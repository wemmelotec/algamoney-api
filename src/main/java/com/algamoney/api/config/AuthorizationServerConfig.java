package com.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.algamoney.api.config.token.CustomTokenEnhancer;

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	// configurar a aplicação (o cliente) que é diferente do usuário
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
			.withClient("angular")
			.secret("$2a$10$qVWS0VuXTCJYkLpk2W0K4OuxS/LhDtZYCmd9FEqKdN2p85GA6hzdu")
			.scopes("read", "write")
			.authorizedGrantTypes("password","refresh_token")
			.accessTokenValiditySeconds(1500)
		    .refreshTokenValiditySeconds(3600 * 24)//tempo de vida do refresh token um dia
		.and()
			.withClient("mobile")
			.secret("$2a$10$r16T/nUVT3zKbvqSGfyk8.cZXzdTgaCimllns5qQwjWFusDSnmJHa")
			.scopes("read")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(1800)
			.refreshTokenValiditySeconds(3600 * 24);
		
	}

	//método para customizar o token
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
				.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancerChain)
				.reuseRefreshTokens(false)
				.authenticationManager(authenticationManager);
	}
	/*
	 * criei esse método e anotei como Bean para poder injetar ele em qualquer lugar
	 * que eu precisar na aplicacao por isso eu criei esse metodo que retorna o tipo
	 * de instacia que eu quero
	 */
	@Bean//quem precisar de um access token converte basta chamar ele
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
	}

	// criado para armazenar o token
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
	    return new CustomTokenEnhancer();
	}
}
