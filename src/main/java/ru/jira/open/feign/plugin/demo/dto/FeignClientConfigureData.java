package ru.jira.open.feign.plugin.demo.dto;

import feign.RequestInterceptor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeignClientConfigureData {
    private final Class<?> feignClientClazz;
    private final String feignClientUrl;
    private final Iterable<RequestInterceptor> requestInterceptors;
}
