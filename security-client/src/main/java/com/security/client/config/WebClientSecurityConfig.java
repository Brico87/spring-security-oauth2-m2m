package com.security.client.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;

@Configuration
public class WebClientSecurityConfig {

	@Bean
	public WebClientSecurityCustomizer webClientSecurityCustomizer(
			ReactiveClientRegistrationRepository clientRegistrations) {

		// Provides support for an unauthenticated user such as an application
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrations, new UnAuthenticatedServerOAuth2AuthorizedClientRepository());

		// Build up a new WebClientCustomizer implementation to inject the oauth filter
		// function into the WebClient.Builder instance
		return new WebClientSecurityCustomizer(oauth);
	}

	/**
	 * Helper function to include the Spring CLIENT_REGISTRATION_ID_ATTR_NAME in a
	 * properties Map
	 * 
	 * @param provider - OAuth2 authorization provider name
	 * @return consumer properties Map
	 */
	public static Consumer<java.util.Map<java.lang.String, java.lang.Object>> getExchangeFilterWith(String provider) {
		return ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(provider);
	}
}
