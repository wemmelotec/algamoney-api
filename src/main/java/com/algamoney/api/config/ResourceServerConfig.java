package com.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration // indica que essa é uma classe de configuração
@EnableWebSecurity // habilita essa classe para segurança
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) //para habilitar as permissões
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired // para injetar o AuthenticationManagerBuilder
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// buscar autenticação em memória
		auth//.inMemoryAuthentication().withUser("admin").password("admin").roles("ROLE");
		    .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// para categorias qualquer um pode acessar para o resto eu tenho que estar
		// autenticado
		http.authorizeRequests()
			//.antMatchers("/categorias").permitAll()
			.anyRequest()
			.authenticated()
			.and()// e
			// .httpBasic().and()//tipo de autenticação http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()// nossa API não mantém
			.csrf().disable();// como eu não uso JS na aplicação eu posso desabilitar
	}
	
	//para deixar STATELESS, ou seja, a parte de segurança eu não quero que deixe nenhum estado, ou seja, não armazena nada
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true);
    }
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}

}