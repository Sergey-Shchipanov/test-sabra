package com.shchipanov.testsabra;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


@Component
@Slf4j
public class ElasticsearchSender {

    public void pullData(List<GoogleSearchResultItem> data) throws IOException, ExecutionException, InterruptedException {
        Client client = new PreBuiltTransportClient(
                Settings.builder().put("cluster.name", "docker-cluster")
                        .build())
                .addTransportAddress(new TransportAddress(InetAddress.getByName(System.getProperty("sabra.elastic.ip")), 9300));

        var checkIndex = client.admin().indices().exists(new IndicesExistsRequest("results"));

        if (checkIndex.isDone()) {
            var result = checkIndex.get();
            if ( !result.isExists() ) {
                createIndexInElastic(client);
            }
        }

        pushToElastic(data, client);
    }

    private void createIndexInElastic(Client client) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("results");


        XContentBuilder builder = prepareMapping();

        request.mapping("_doc", builder);

        client.admin().indices().create(request, new ActionListener<>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
            }

            @Override
            public void onFailure(Exception e) {
                throw new IllegalStateException("Can't create index");
            }
        });
    }

    private void pushToElastic(List<GoogleSearchResultItem> data, Client client) throws IOException {
        for (GoogleSearchResultItem res : data) {
            IndexRequest request = client.prepareIndex("results", "_doc", UUID.randomUUID().toString())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", res.getTitle())
                            .field("link", res.getLink())
                            .field("cacheId", res.getCacheId())
                            .field("displayLink", res.getDisplayLink())
                            .endObject()
                    ).request();

            client.index(request, new ActionListener<>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                }

                @Override
                public void onFailure(Exception e) {
                    throw new IllegalStateException(e);
                }
            });
        }
    }

    private XContentBuilder prepareMapping() throws IOException {
        XContentBuilder builder = jsonBuilder();
        builder.startObject();
        {
            builder.startObject("_doc");
            {
                builder.startObject("properties");
                {
                    builder.startObject("id");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    builder.startObject("title");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    builder.startObject("link");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    builder.startObject("cacheId");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    builder.startObject("displayLink");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        return builder;
    }
}
