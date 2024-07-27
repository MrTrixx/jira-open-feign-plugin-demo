package ru.jira.open.feign.plugin.demo.service;

import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.osgi.factory.OsgiPlugin;
import lombok.RequiredArgsConstructor;
import org.osgi.framework.Bundle;

import java.util.Objects;

@RequiredArgsConstructor
public class JiraOsgiBundleAccessor {
    private final PluginAccessor pluginAccessor;

    public Bundle getJiraPluginAsBundle(String pluginKey) {
        OsgiPlugin plugin = (OsgiPlugin) this.pluginAccessor.getPlugin(pluginKey);
        if (Objects.isNull(plugin)) {
            throw new IllegalArgumentException(String.format("Plugin not with key %s not found", pluginKey));
        }
        return plugin.getBundle();
    }

}
