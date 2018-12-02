package com.security.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class ServerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

	@GetMapping(value = "/principal", produces = { MediaType.TEXT_EVENT_STREAM_VALUE,
			MediaType.APPLICATION_STREAM_JSON_VALUE })
	public Mono<String> getPrincipal() {

		// @formatter:off
		return ReactiveSecurityContextHolder.getContext()
				 .map(SecurityContext::getAuthentication)
				 .map(Authentication::getPrincipal)
				 .map(principal -> {
					 String name = (String) principal;
					 LOGGER.info("App authentified : '{}'", name);
					 return name;
				 });
		// @formatter:on
	}
}
