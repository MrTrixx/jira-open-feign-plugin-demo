package ru.jira.open.feign.plugin.demo.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/healthCheck")
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheckResource {

    @GET
    public String healthCheck() {
        return "OK";
    }
}
