package com.shchipanov.testsabra;

import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleCaller {
    private static final String API_KEY = "AIzaSyAMDVY9urdpX7Dj_be5TDO9-8YojaBIYDc";
    private static final String CX = "012351567661980393061:dii7rmsns_m";

    public List<GoogleSearchResultItem> getSearchResultFromGoogle() {

        List<GoogleSearchResultItem> results = new ArrayList<>();

        Client client = ClientBuilder.newClient();
        GoogleCustomSearchResult response = client.target("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", API_KEY)
                .queryParam("cx", CX)
                .queryParam("q", "Triton+malware")
                .queryParam("num", "10")
                .request(MediaType.APPLICATION_JSON)
                .get(GoogleCustomSearchResult.class);

        results.addAll(response.getItems());

        for (int i = 1; i <= 3; i++) {
            results.addAll(client.target("https://www.googleapis.com/customsearch/v1")
                    .queryParam("key", API_KEY)
                    .queryParam("cx", CX)
                    .queryParam("q", "Triton+malware")
                    .queryParam("num", "10")
                    .queryParam("start", i + "1")
                    .request(MediaType.APPLICATION_JSON)
                    .get(GoogleCustomSearchResult.class).getItems());
        }
        return results;
    }
}
