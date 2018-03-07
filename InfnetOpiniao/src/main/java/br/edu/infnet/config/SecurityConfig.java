package br.edu.infnet.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	protected void configure(HttpSecurity http) throws Exception {
		/* AUTENTICAÇÃO VIA LOGIN */
		http
		.authorizeRequests()
			.antMatchers("/images/**","/css/**", "/js/**", "/webjars/**", "/login/**", "/answer/**", "/logout").permitAll()
			.anyRequest().authenticated()
		.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/home",true)
				.usernameParameter("login")
				.passwordParameter("senha")
				.failureUrl("/login/erro")
		.and().logout().    //logout configuration
			   logoutUrl("/logout"). 
		       logoutSuccessUrl("/login")		
		.and()
			.httpBasic();
		
		http.csrf().disable();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("tricolor").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("user").password("tricolor").roles("USER");	
	}	
	
} 