package ru.jira.open.feign.plugin.demo.spring.context;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.osgi.framework.Bundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import ru.jira.open.feign.plugin.demo.dto.FeignClientConfigureData;
import ru.jira.open.feign.plugin.demo.exception.ClassNotFoundExceptionWrapper;
import ru.jira.open.feign.plugin.demo.exception.ClientsNotConfigureException;
import ru.jira.open.feign.plugin.demo.exception.IllegalSpringPropertyException;
import ru.jira.open.feign.plugin.demo.service.JiraOsgiBundleAccessor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class FeignClientBeanRegister implements BeanFactoryPostProcessor {

    private final FeignClientConfigurePropertiesFinder feignClientConfigurePropertiesFinder;
    private final JiraOsgiBundleAccessor jiraOsgiBundleAccessor;
    private final JiraOsgiSpringContextResourcePatternResolverAccessor jiraOsgiSpringContextResourcePatternResolverAccessor;
    private final FeignClientBuilder feignClientBuilder;
    private final String pluginKey;
    private final String packageToFindFeignClients;

    private ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider;

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory configurableListableBeanFactory)
            throws BeansException {
        try {
            configureClassPathScanningCandidateComponentProvider();
            findAndRegisterFeignClientData(configurableListableBeanFactory);
        } catch (Exception e) {
            throw new ClientsNotConfigureException(e);
        }
    }

    private void configureClassPathScanningCandidateComponentProvider() throws IllegalAccessException {
        Bundle jiraPluginAsBundle = jiraOsgiBundleAccessor.getJiraPluginAsBundle(pluginKey);
        ResourcePatternResolver jiraResourcePatternResolverImplementation
                = jiraOsgiSpringContextResourcePatternResolverAccessor.getJiraResourcePatternResolverImplementation(jiraPluginAsBundle);
        classPathScanningCandidateComponentProvider = new CustomClassPathScanningCandidateComponentProvider(false);

        Class<? extends ClassPathScanningCandidateComponentProvider> providerClazz
                = ClassPathScanningCandidateComponentProvider.class;
        Field providerClazzField = Arrays.stream(providerClazz.getDeclaredFields())
                                         .filter(field -> field.getType() == ResourcePatternResolver.class)
                                         .findFirst()
                                         .orElseThrow(() -> new RuntimeException("Field of type ResourcePatternResolver not found"));
        providerClazzField.setAccessible(true);
        providerClazzField.set(classPathScanningCandidateComponentProvider, jiraResourcePatternResolverImplementation);

        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class, false, true));
    }

    private void findAndRegisterFeignClientData(
            final ConfigurableListableBeanFactory configurableListableBeanFactory) {
        Map<String, String> feignUrls = feignClientConfigurePropertiesFinder.findFeignUrlsProperties();
        Set<BeanDefinition> candidateComponents
                = classPathScanningCandidateComponentProvider.findCandidateComponents(packageToFindFeignClients);

        validateFoundFeignClientData(candidateComponents, feignUrls);

        try {
            for (BeanDefinition candidateComponent : candidateComponents) {
                String feignClientClazzName = candidateComponent.getBeanClassName();
                Class<?> clientClazz = Class.forName(feignClientClazzName);
                FeignClient annotation = clientClazz.getDeclaredAnnotation(FeignClient.class);
                String urlParamInAnnotation = annotation.url();
                String urlValueInEnv = feignUrls.get(urlParamInAnnotation);
                if (Objects.isNull(urlValueInEnv)) {
                    throw new IllegalArgumentException("Url value must be not null");
                }

                Class<?>[] feignClientLazySpringConfigs = annotation.config();
                List<RequestInterceptor> requestInterceptors = new ArrayList<>();
                if (feignClientLazySpringConfigs != null) {
                    for (Class<?> feignClientLazySpringConfigClazz : feignClientLazySpringConfigs) {
                        // activate lazy configuratio
                        configurableListableBeanFactory.getBean(feignClientLazySpringConfigClazz);
                        requestInterceptors.addAll(configurableListableBeanFactory.getBeansOfType(RequestInterceptor.class).values());
                    }
                }

                Object client
                        = feignClientBuilder.build(
                                FeignClientConfigureData.builder()
                                                        .feignClientClazz(clientClazz)
                                                        .feignClientUrl(urlValueInEnv)
                                                        .requestInterceptors(requestInterceptors)
                                                        .build()
                );
                configurableListableBeanFactory.registerSingleton(annotation.value(), client);
            }
        } catch (ClassNotFoundException e) {
            // unexpected behavior
            throw new ClassNotFoundExceptionWrapper(e);
        } catch (Exception e) {
            log.error("Error happened creating feign client", e);
            throw new RuntimeException(e);
        }
    }

    private void validateFoundFeignClientData(
            final Set<BeanDefinition> candidateComponents,
            final Map<String, String> feignUrls) {
        if (!candidateComponents.isEmpty() && feignUrls.isEmpty()) {
            throw new IllegalSpringPropertyException("Defined Feign client not have properties in Environment");
        }
    }

}
