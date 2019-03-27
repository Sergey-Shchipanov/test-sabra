package com.shchipanov.testsabra;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@Slf4j
public class ElasticSearchPuller {

    public void pullData(List<GoogleSearchResultItem> data) throws UnknownHostException {
        Client client = new PreBuiltTransportClient(
                Settings.builder().put("cluster.name", "docker-cluster")
                        .build())
                .addTransportAddress(new TransportAddress(InetAddress.getByName(System.getProperty("sabra.elastic.ip")), 9300));

        CreateIndexRequest request = new CreateIndexRequest("results");


        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("id", "id");
        mapping.put("title", "title");
        mapping.put("link", "link");
        mapping.put("cacheId", "cacheId");
        mapping.put("displayLink", "displayLink");
        jsonMap.put("_doc", mapping);
        request.mapping("_doc", jsonMap);

        client.admin().indices().create(request, new ActionListener<>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {

            }

            @Override
            public void onFailure(Exception e) {
                throw new IllegalStateException("Can't create index");
            }
        });

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
