package com.salesianostriana.dam.primerproyectogrupo6.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailsService;
	
    CustomSuccessHandler customSuccessHandler;

	public SecurityConfig(UserDetailsService userDetailsService, CustomSuccessHandler customSuccessHandler) {
		this.userDetailsService = userDetailsService;
		this.customSuccessHandler = customSuccessHandler;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		
	}
	
	 @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) 
	      throws Exception {
	        auth
	          .inMemoryAuthentication()
	          .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
	          .and()
	          .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
	    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/css/**","/js/**","/webjars/**", "/h2-console/**", "/registro/**", "/img/**").permitAll()
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				.antMatchers("/superAdmin/**").hasAnyRole("SUPER")
				.antMatchers("/usuario/**").hasAnyRole("USER")
				.antMatchers("/noValidate/**").hasAnyRole("NO_VALIDATE")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.successHandler(customSuccessHandler)
				.and()
			.logout()
				.logoutUrl("/logout")
				.permitAll()
				.and()
			.exceptionHandling()
				.accessDeniedPage("/acceso-denegado");
		
		// Añadimos esto para poder seguir accediendo a la consola de H2
		// con Spring Security habilitado.
		http.csrf().disable();
        http.headers().frameOptions().disable();
		
	}
}