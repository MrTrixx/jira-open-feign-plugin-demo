package ru.jira.open.feign.plugin.demo.spring.context;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

public class CustomClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {
    private static final String FEIGN_CLIENT_AUTO_CONFIGURE_ATTR_NAME = "autoConfigure";

    public CustomClassPathScanningCandidateComponentProvider(final boolean useDefaultFilters) {
        super(useDefaultFilters);
    }

    @Override
    protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
        return isInterfaceAndAnnotatedByFeignClientAnnotationToAutoConfigure(beanDefinition);
    }

    private boolean isInterfaceAndAnnotatedByFeignClientAnnotationToAutoConfigure(final AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface()
                && beanDefinition.getMetadata().isAnnotated(FeignClient.class.getName())
                && isAutoConfigureFeignClient(beanDefinition);
    }

    private static boolean isAutoConfigureFeignClient(final AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata()
                             .getAnnotations()
                             .get(FeignClient.class)
                             .getBoolean(FEIGN_CLIENT_AUTO_CONFIGURE_ATTR_NAME);
    }
}
