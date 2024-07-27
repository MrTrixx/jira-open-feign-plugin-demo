package ru.jira.open.feign.plugin.demo.spring.context;

import lombok.RequiredArgsConstructor;
import org.osgi.framework.Bundle;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.jira.open.feign.plugin.demo.exception.OsgiBundleResourcePatternResolverNotCreatedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
public class JiraOsgiSpringContextResourcePatternResolverAccessor {
    private static final String OSGI_BUNDLE_RESOURCE_PATTER_RESOLVER_CLAZZ_NAME
            = "org.eclipse.gemini.blueprint.io.OsgiBundleResourcePatternResolver";

    private final ApplicationContext applicationContext;

    public ResourcePatternResolver getJiraResourcePatternResolverImplementation(final Bundle bundle) {
        try {
            Class<?> jiraOsgiResourcePatterResolverClazz = findJiraOsgiResourcePatterResolverClazzByAppContextClassLoader();
            Constructor<?> osgiBundleResourcePatternResolverConstructor
                    = jiraOsgiResourcePatterResolverClazz.getDeclaredConstructor(Bundle.class);
            Object osgiBundleResourcePatternResolver = osgiBundleResourcePatternResolverConstructor.newInstance(bundle);
            return (ResourcePatternResolver) osgiBundleResourcePatternResolver;
        } catch (NoSuchMethodException |
                 ClassNotFoundException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new OsgiBundleResourcePatternResolverNotCreatedException(e);
        }
    }

    private Class<?> findJiraOsgiResourcePatterResolverClazzByAppContextClassLoader() throws ClassNotFoundException {
        return applicationContext.getClass()
                                 .getClassLoader()
                                 .loadClass(OSGI_BUNDLE_RESOURCE_PATTER_RESOLVER_CLAZZ_NAME);
    }
}
