package com.security.client.config;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient.Builder;

public class WebClientSecurityCustomizer implements WebClientCustomizer {

	private ServerOAuth2AuthorizedClientExchangeFilterFunction securityExchangeFilterFunction;

	public WebClientSecurityCustomizer(
			ServerOAuth2AuthorizedClientExchangeFilterFunction securityExchangeFilterFunction) {
		this.securityExchangeFilterFunction = securityExchangeFilterFunction;
	}

	@Override
	public void customize(Builder webClientBuilder) {
		// Add security exchange filter function to Builder filters list
		webClientBuilder.filters((filterFunctions) -> {
			if (!filterFunctions.contains(this.securityExchangeFilterFunction)) {
				filterFunctions.add(0, this.securityExchangeFilterFunction);
			}
		});
	}
}
