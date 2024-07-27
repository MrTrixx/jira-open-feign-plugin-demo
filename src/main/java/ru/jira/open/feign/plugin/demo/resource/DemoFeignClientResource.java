package ru.jira.open.feign.plugin.demo.resource;

import lombok.extern.slf4j.Slf4j;
import ru.jira.open.feign.plugin.demo.client.CurrentJiraPluginRestClient;
import ru.jira.open.feign.plugin.demo.dto.SomeRequestDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/demo-feign")
@Slf4j
public class DemoFeignClientResource {
    /*
        This OpenFeign client generated in FeignClientBuilder and
        registered in Plugin Spring Context by FeignClientBeanRegister
        Custom Jira Plugin Rest Module is lazy and
        injecting bean will be after first calling any Custom Jira Plugin Endpoint
     */
    private final CurrentJiraPluginRestClient currentJiraPluginRestClient;

    public DemoFeignClientResource(final CurrentJiraPluginRestClient currentJiraPluginRestClient) {
        this.currentJiraPluginRestClient = currentJiraPluginRestClient;
    }

    @GET
    @Path("/call-feign-client-get")
    public void checkWorkFeignClientOnGET() {
        log.debug("DemoFeignClientResource#checkWorkFeignClientOnGET start");
        // this client method will call DemoFeignClientResource#callTestGetEndpointToCallByFeignClient
        currentJiraPluginRestClient.callTestGetEndpointToCallByFeignClient();
        log.debug("DemoFeignClientResource#checkWorkFeignClientOnGET end");
    }

    @GET
    @Path("/testGET_EndpointToCallByFeignClient")
    public void testGETEndpointToCallByFeignClient() {
        log.debug("testGET_EndpointToCallByFeignClient called by client {}", currentJiraPluginRestClient);
    }

    @POST
    @Path("/call-feign-client-post")
    @Consumes(MediaType.APPLICATION_JSON)
    public void checkWorkFeignClientOnPOST(SomeRequestDto someRequestDto) {
        log.debug("DemoFeignClientResource#checkWorkFeignClientOnPOST start");
        // this client method will call DemoFeignClientResource#testPOSTEndpointToCallByFeignClient
        SomeRequestDto response = currentJiraPluginRestClient.callTestPOSTEndpointToCallByFeignClient(someRequestDto);
        log.debug("called testPOST_EndpointToCallByFeignClient return response {}", response);
        log.debug("DemoFeignClientResource#checkWorkFeignClientOnPOST end");
    }

    @POST
    @Path("/testPOST_EndpointToCallByFeignClient")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SomeRequestDto testPOSTEndpointToCallByFeignClient(SomeRequestDto someRequestDto) {
        log.debug("testPOST_EndpointToCallByFeignClient called by client {} and received json: {}", currentJiraPluginRestClient, someRequestDto);
        return someRequestDto;
    }
}
