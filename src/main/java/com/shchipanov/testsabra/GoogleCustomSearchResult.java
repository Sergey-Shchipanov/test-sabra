package com.shchipanov.testsabra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;

@Data
@JsonComponent
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleCustomSearchResult {

    @JsonProperty("kind")
    private String kind;

    @JsonProperty("items")
    private List<GoogleSearchResultItem> items;
}
