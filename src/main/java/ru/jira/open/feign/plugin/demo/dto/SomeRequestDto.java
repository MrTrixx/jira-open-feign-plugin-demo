package ru.jira.open.feign.plugin.demo.dto;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SomeRequestDto {
    @JsonProperty
    private String strProp;
}
