package com.security.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.security.client.config.ApiConfig;
import com.security.client.config.WebClientSecurityConfig;

@SpringBootApplication(scanBasePackages = "com.security.client")
@EnableConfigurationProperties
public class ClientApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientApplication.class);

	private ApiConfig apiConfig;
	private Builder webClientBuilder;

	@Autowired
	public ClientApplication(WebClient.Builder wcb, ApiConfig apiConfig) {
		this.webClientBuilder = wcb;
		this.apiConfig = apiConfig;
	}

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Exchanges authenticated using WebClient.Builder with OAuth2
		//@formatter:off
		webClientBuilder.build()
						.get()
						.uri(uriBuilder -> 
								uriBuilder.scheme(apiConfig.getScheme())
										  .host(apiConfig.getHost())
										  .port(apiConfig.getPort())
										  .path(apiConfig.getEndpoint())
										  .build()
					    )
						.attributes(WebClientSecurityConfig.getExchangeFilterWith("keycloak"))
						.accept(MediaType.APPLICATION_STREAM_JSON)
						.retrieve()
						.bodyToFlux(String.class)
						.doOnSubscribe(subscription -> LOGGER.info("Connection to security server"))
						.onErrorMap(err -> {
							try {
								// Wait if we receive an error
								Thread.sleep(apiConfig.getDelayBetweenRetries());
							} catch (InterruptedException e) {
								LOGGER.warn(e.toString());
							}
							return err;
						})
						.subscribe(str -> LOGGER.info("Server response : '{}'", str));
		//@formatter:on
	}
}