package com.java.microservice.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class MovieCatalogServiceApplication {

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate(); // this will run only one time
	}

	@Bean
	public WebClient.Builder getWebClientBuilder(){
		return WebClient.builder(); // using webclient for reactive web
	}



	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}
