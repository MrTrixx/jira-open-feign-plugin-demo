package ru.jira.open.feign.plugin.demo.spring.configuration.client;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/*
    This configuration is lazy
    and need activate it in Plugin Spring context by ru.jira.open.feign.plugin.demo.spring.context.FeignClientBeanRegister
    after collecting all needed items for customisation feign client as interface
 */
@Lazy
@Configuration
public class CurrentJiraPluginRestClientConfiguration {

    @Bean
    RequestInterceptor jiraBasicAuthRequestInterceptor(
            @Value("${current.jira.plugin.client.login}")
            final String login,
            @Value("${current.jira.plugin.client.pass}")
            final String pass) {
        return new BasicAuthRequestInterceptor(login, pass);
    }

}
