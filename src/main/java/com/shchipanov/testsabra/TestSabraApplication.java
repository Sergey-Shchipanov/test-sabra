package com.shchipanov.testsabra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestSabraApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TestSabraApplication.class, args);

		var searchResults = context.getBean(GoogleCaller.class).getSearchResultFromGoogle();
		context.getBean(ElasticSearchPuller.class).pullData(searchResults);
	}

}
