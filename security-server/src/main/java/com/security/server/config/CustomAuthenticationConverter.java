package com.security.server.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import com.nimbusds.jwt.JWTClaimsSet;

import reactor.core.publisher.Mono;

public class CustomAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationConverter.class);
	private String identityClaimLabel;

	public CustomAuthenticationConverter(String identityClaimLabel) {
		this.identityClaimLabel = identityClaimLabel;
	}

	public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
		return Mono.justOrEmpty(doConversion(jwt));
	}

	private AbstractAuthenticationToken doConversion(Jwt token) {

		// Extract claims set from signed JWT object
		Map<String, Object> claimsSet = token.getClaims();

		// Extract identity from claims set
		String identity = (String) claimsSet.get(identityClaimLabel);

		if (StringUtils.hasText(identity)) {
			// Build JWT claims set object
			JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
			claimsSet.forEach((name, value) -> jwtClaimsSetBuilder.claim(name, value));
			JWTClaimsSet jwtClaimsSet = jwtClaimsSetBuilder.build();

			// Return authentication object with holding JWT data
			return new JwtAuthentication(identity, jwtClaimsSet, null);
		} else {
			LOGGER.error("Missing '{}' field or value is not valid in the JWT token", identityClaimLabel);
			return getNonAuthenticatedToken(null);
		}
	}

	private static AbstractAuthenticationToken getNonAuthenticatedToken(String identity) {
		String anonymousRole = "anonymous";
		String principal = StringUtils.hasText(identity) ? identity : anonymousRole;
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(anonymousRole));
		AnonymousAuthenticationToken token = new AnonymousAuthenticationToken(anonymousRole, principal, authorities);
		token.setAuthenticated(false);
		return token;
	}

	@SuppressWarnings("serial")
	private static class JwtAuthentication extends AbstractAuthenticationToken {

		private final transient Object principal;
		private JWTClaimsSet jwtClaimsSet;

		public JwtAuthentication(Object principal, JWTClaimsSet jwtClaimsSet,
				Collection<? extends GrantedAuthority> authorities) {
			super(authorities);
			this.principal = principal;
			this.jwtClaimsSet = jwtClaimsSet;
			super.setAuthenticated(true);
		}

		@Override
		public Object getCredentials() {
			return null;
		}

		@Override
		public Object getPrincipal() {
			return principal;
		}

		public JWTClaimsSet getJwtClaimsSet() {
			return jwtClaimsSet;
		}
	}
}
