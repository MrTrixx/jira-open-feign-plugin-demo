package ru.jira.open.feign.plugin.demo.client;

import feign.Headers;
import feign.RequestLine;
import ru.jira.open.feign.plugin.demo.dto.SomeRequestDto;
import ru.jira.open.feign.plugin.demo.spring.configuration.client.CurrentJiraPluginRestClientConfiguration;
import ru.jira.open.feign.plugin.demo.spring.context.FeignClient;

@FeignClient(
        value = "local-jira",
        url = "feign.url.local-jira",
        config = CurrentJiraPluginRestClientConfiguration.class,
        autoConfigure = true
)
public interface CurrentJiraPluginRestClient {

    @RequestLine("GET /demo-feign/testGET_EndpointToCallByFeignClient")
    void callTestGetEndpointToCallByFeignClient();

    @RequestLine("POST /demo-feign/testPOST_EndpointToCallByFeignClient")
    @Headers("Content-Type: application/json")
    SomeRequestDto callTestPOSTEndpointToCallByFeignClient(SomeRequestDto someRequestDto);

}
