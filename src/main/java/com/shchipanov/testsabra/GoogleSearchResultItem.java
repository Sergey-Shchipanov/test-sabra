package com.shchipanov.testsabra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

@Data
@JsonComponent
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleSearchResultItem {

    @JsonProperty("title")
    private String title;

    @JsonProperty("link")
    private String link;

    @JsonProperty("displayLink")
    private String displayLink;

    @JsonProperty("cacheId")
    private String cacheId;
}
