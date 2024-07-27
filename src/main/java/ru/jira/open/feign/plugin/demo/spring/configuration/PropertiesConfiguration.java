package ru.jira.open.feign.plugin.demo.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources(
        value = @PropertySource("osgibundlejar:/ru/jira/open/feign/plugin/demo/spring-plugin.properties")
)
public class PropertiesConfiguration {

    /**
     This bean helps to resolve properties from /ru/jira/open/feign/plugin/demo/spring-plugin.properties
     I don't have reason why properties not resolved by default spring mechanism

     I researched next in debug mode:
     If comment bean: ru.jira.open.feign.plugin.demo.spring.context.FeignClientBeanRegister
     in configuration class ru.jira.open.feign.plugin.demo.spring.configuration.CustomJiraPluginFeignConfiguration
     properties normal resolved without bean org.springframework.context.support.PropertySourcesPlaceholderConfigurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
