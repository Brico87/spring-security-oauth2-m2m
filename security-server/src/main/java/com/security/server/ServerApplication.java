package com.security.server;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableWebFluxSecurity
public class ServerApplication {

	/**
	 * Launch the application.
	 * 
	 * @param args The program arguments.
	 */
	public static void main(String[] args) {

		ConfigurableApplicationContext context = new SpringApplicationBuilder().sources(ServerApplication.class)
				// De-activate the web container
				.web(WebApplicationType.REACTIVE)
				// create run context
				.run(args);

		// Start the application context
		context.start();
	}
}
