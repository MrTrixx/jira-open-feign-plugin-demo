package ru.jira.open.feign.plugin.demo.spring.configuration;

import com.atlassian.plugins.osgi.javaconfig.configs.beans.PluginAccessorBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PluginAccessorBean.class)
public class CommonConfiguration {
}
