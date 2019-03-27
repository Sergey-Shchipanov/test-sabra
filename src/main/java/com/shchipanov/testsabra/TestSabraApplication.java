package com.shchipanov.testsabra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.UnknownHostException;

@SpringBootApplication
public class TestSabraApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(TestSabraApplication.class, args);

		try {
			System.setProperty("sabra.elastic.ip", args[0]);
			System.setProperty("sabra.elastic.port", args[1]);
		} catch (Exception e) {
			System.out.println(e);
		}

		var searchResults = context.getBean(GoogleCaller.class).getSearchResultFromGoogle();
		context.getBean(ElasticSearchPuller.class).pullData(searchResults);
	}

}
