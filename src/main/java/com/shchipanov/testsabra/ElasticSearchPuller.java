package com.shchipanov.testsabra;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;


@Component
@Slf4j
public class ElasticSearchPuller {

    public void pullData(List<GoogleSearchResultItem> data) throws UnknownHostException {
            Client client = new PreBuiltTransportClient(
                    Settings.builder().put("client.transport.sniff", true)
                            .build())
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(System.getProperty("sabra.elastic.ip")), 9300));

            BulkRequest bulkRequest = new BulkRequest();

            data.forEach(res -> bulkRequest.add(
                    new IndexRequest("results").id(String.valueOf(UUID.randomUUID()))
                            .source("title", res.getTitle())
                            .source("link", res.getLink())
                            .source("cacheId", res.getCacheId())
                            .source("displayLink", res.getDisplayLink())));

            client.bulk(bulkRequest);
    }
}
