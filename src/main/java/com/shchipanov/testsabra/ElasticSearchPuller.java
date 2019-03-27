package com.shchipanov.testsabra;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


@Component
@Slf4j
public class ElasticSearchPuller {

    public void pullData(List<GoogleSearchResultItem> data) throws IOException {
        Client client = new PreBuiltTransportClient(
                Settings.builder().put("cluster.name", "docker-cluster")
                        .build())
                .addTransportAddress(new TransportAddress(InetAddress.getByName(System.getProperty("sabra.elastic.ip")), 9300));

        CreateIndexRequest request = new CreateIndexRequest("results");


        XContentBuilder builder = prepareMapping();

        request.mapping("_doc", builder);

        client.admin().indices().create(request, new ActionListener<>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
                throw new IllegalStateException("Can't create index");
            }
        });

        BulkRequest bulkRequest = new BulkRequest();


        prepareBulk(data, client, bulkRequest);

        client.bulk(bulkRequest, new ActionListener<>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
                throw new IllegalStateException(e);
            }
        });
    }

    private void prepareBulk(List<GoogleSearchResultItem> data, Client client, BulkRequest bulkRequest) throws IOException {
        for (GoogleSearchResultItem res : data) {
            bulkRequest.add(client.prepareIndex("results", "_doc", UUID.randomUUID().toString())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", res.getTitle())
                            .field("link", res.getLink())
                            .field("cacheId", res.getCacheId())
                            .field("displayLink", res.getDisplayLink())
                            .field("date", new Date())
                            .endObject()
                    ).request()
            );
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
                    builder.startObject("date");
                    {
                        builder.field("type", "date");
                        builder.field("format", "yyyy-MM-dd");
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
