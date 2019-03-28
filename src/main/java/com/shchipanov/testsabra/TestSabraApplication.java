package com.shchipanov.testsabra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class TestSabraApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(TestSabraApplication.class, args);

        System.setProperty("sabra.elastic.ip", args[0]);
        System.setProperty("sabra.elastic.port", args[1]);

        var searchResults = context.getBean(GoogleCaller.class).getSearchResultFromGoogle();
        context.getBean(ElasticsearchSender.class).pullData(searchResults);
    }

}
