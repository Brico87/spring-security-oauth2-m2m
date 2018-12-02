package com.security.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.api")
public class ApiConfig {

	String scheme;
	String host;
	Integer port;
	String endpoint;
	Integer delayBetweenRetries;

	public String getScheme() {
		return scheme;
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Integer getDelayBetweenRetries() {
		return delayBetweenRetries;
	}

	public void setDelayBetweenRetries(Integer delayBetweenRetries) {
		this.delayBetweenRetries = delayBetweenRetries;
	}
}
