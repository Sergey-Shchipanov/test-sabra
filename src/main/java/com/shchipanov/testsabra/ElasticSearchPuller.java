package com.shchipanov.testsabra;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;


@Component
@Slf4j
public class ElasticSearchPuller {

    public void pullData(List<GoogleSearchResultItem> data) {
        try {
            Client client = new PreBuiltTransportClient(
                    Settings.builder().put("client.transport.sniff", true)
                            .put("cluster.name", "elasticsearch").build())
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9200));

            BulkRequestBuilder bulkRequest = client.prepareBulk();

            bulkRequest.add(new IndexRequest("results").id("1")
                    .source(XContentType.values(), data));
        } catch(Exception e) {
            log.error("Something goes wrong", e);
        }
    }
}
