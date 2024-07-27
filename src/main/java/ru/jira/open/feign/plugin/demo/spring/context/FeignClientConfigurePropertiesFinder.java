package ru.jira.open.feign.plugin.demo.spring.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import ru.jira.open.feign.plugin.demo.exception.IllegalSpringPropertyException;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Slf4j
public class FeignClientConfigurePropertiesFinder {
    private static final String FEIGN_URLS_PROPERTIES_PREFIX = "feign.url";

    private final ConfigurableEnvironment environment;

    @NotNull
    public Map<String, String> findFeignUrlsProperties() {
        List<EnumerablePropertySource> resourceProperties = getResourceProperties();
        Map<String, String> feignUrls = new HashMap<>();
        for (EnumerablePropertySource resourcePropertySource : resourceProperties) {
            String[] propertyNames = resourcePropertySource.getPropertyNames();
            for (String propertyName : propertyNames) {
                if (!propertyName.startsWith(FEIGN_URLS_PROPERTIES_PREFIX)) {
                    continue;
                }

                Object property = resourcePropertySource.getProperty(propertyName);
                if (Objects.isNull(property)) {
                    throw new IllegalSpringPropertyException(String.format("Property %s must have value!!!", propertyName));
                }

                String propertyAsUrl = property.toString();
                if (!isPropertyAsUrl(propertyAsUrl)) {
                    throw new IllegalSpringPropertyException(String.format(
                            "Property %s must be url %s!!!",
                            propertyName,
                            propertyAsUrl)
                    );
                }

                feignUrls.put(propertyName, propertyAsUrl);
            }
            return feignUrls;

        }

        return feignUrls;
    }

    private boolean isPropertyAsUrl(final String propertyAsUrl) {
        try {
            new URL(propertyAsUrl).toURI();
            return true;
        } catch (Exception e) {
            log.debug("property {} not url", propertyAsUrl, e);
            return false;
        }
    }

    private List<EnumerablePropertySource> getResourceProperties() {
        MutablePropertySources propertySources = environment.getPropertySources();
        return propertySources.stream()
                              .filter(not(SystemEnvironmentPropertySource.class::isInstance))
                              .filter(EnumerablePropertySource.class::isInstance)
                              .map(EnumerablePropertySource.class::cast)
                              .filter(not(this::isPropertiesSystem))
                              .collect(Collectors.toUnmodifiableList());
    }

    private boolean isPropertiesSystem(EnumerablePropertySource<?> enumerablePropertySource) {
        return "systemProperties".equals(enumerablePropertySource.getName());
    }
}
