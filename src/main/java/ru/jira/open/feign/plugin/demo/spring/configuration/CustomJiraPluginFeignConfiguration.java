package ru.jira.open.feign.plugin.demo.spring.configuration;

import com.atlassian.plugin.PluginAccessor;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import ru.jira.open.feign.plugin.demo.service.JiraOsgiBundleAccessor;
import ru.jira.open.feign.plugin.demo.spring.context.FeignClientBeanRegister;
import ru.jira.open.feign.plugin.demo.spring.context.FeignClientBuilder;
import ru.jira.open.feign.plugin.demo.spring.context.FeignClientConfigurePropertiesFinder;
import ru.jira.open.feign.plugin.demo.spring.context.JiraOsgiSpringContextResourcePatternResolverAccessor;

@Configuration
public class CustomJiraPluginFeignConfiguration {

    @Bean
    Client client() {
        return new OkHttpClient();
    }

    @Bean
    Decoder jacksonDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    Encoder jacksonEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    FeignClientBuilder feignClientBuilder(final Client client,
            final Decoder jacksonDecoder,
            final Encoder jacksonEncoder) {
        return new FeignClientBuilder(client, jacksonDecoder, jacksonEncoder);
    }

    @Bean
    JiraOsgiBundleAccessor jiraOsgiBundleAccessor(final PluginAccessor pluginAccessor) {
        return new JiraOsgiBundleAccessor(pluginAccessor);
    }

    @Bean
    JiraOsgiSpringContextResourcePatternResolverAccessor jiraOsgiSpringContextResourcePatternResolverAccessor(
            final ApplicationContext applicationContext) {
        return new JiraOsgiSpringContextResourcePatternResolverAccessor(applicationContext);
    }

    @Bean
    FeignClientConfigurePropertiesFinder feignClientConfigurePropertiesFinder(
            final ConfigurableEnvironment configurableEnvironment) {
        return new FeignClientConfigurePropertiesFinder(configurableEnvironment);
    }

    @Bean
    BeanFactoryPostProcessor feignBeanDefinitionBuilder(
            final FeignClientConfigurePropertiesFinder feignClientConfigurePropertiesFinder,
            final JiraOsgiBundleAccessor jiraOsgiBundleAccessor,
            final JiraOsgiSpringContextResourcePatternResolverAccessor jiraOsgiSpringContextResourcePatternResolverAccessor,
            final FeignClientBuilder feignClientBuilder,
            @Value("${plugin.key}")
            final String pluginKey,
            @Value("${package.with.feign.clients}")
            final String packageToFindFeignClients
    ) {
        return new FeignClientBeanRegister(
                feignClientConfigurePropertiesFinder,
                jiraOsgiBundleAccessor,
                jiraOsgiSpringContextResourcePatternResolverAccessor,
                feignClientBuilder,
                pluginKey,
                packageToFindFeignClients
        );
    }

}
