package com.security.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableConfigurationProperties(ServerSecurityProperties.class)
public class ServerSecurityConfig {

	private ServerSecurityProperties properties;

	@Autowired
	public ServerSecurityConfig(ServerSecurityProperties properties) {
		this.properties = properties;
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		// Instanciate a new custom authentication converter
		CustomAuthenticationConverter customJwtAuthConverter = new CustomAuthenticationConverter(
				properties.getIdentityClaimLabel());

		//@formatter:off
		http
			// Permit all access to Actuator endpoints
			.authorizeExchange()
				.pathMatchers("/actuator/**")
				.permitAll()
			.and()
			// Restrict exchanges on path matchers specified
			.authorizeExchange()
				.pathMatchers(properties.getAuthenticatedPathMatchers().stream().toArray(String[]::new))
				.authenticated()
				.and()
			// On authenticated path, add a JWT authentication
			.oauth2ResourceServer()
				.jwt()
					.jwtAuthenticationConverter(customJwtAuthConverter);
		//@formatter:on

		return http.build();
	}
}
