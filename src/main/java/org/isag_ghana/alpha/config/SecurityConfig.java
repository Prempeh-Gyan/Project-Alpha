package org.isag_ghana.alpha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@ComponentScan({ "org.isag_ghana.alpha" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		log.info("Ignoring Http request...");
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/resources/**").antMatchers("/js/**")
				.antMatchers("/css/**").antMatchers("/h2/**").antMatchers("/wro/*");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		super.configure(auth);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		log.info("Analyzing Http request...");
		http.authorizeRequests()
				.antMatchers("/", "/viewResources/**", "/index", "/public/**", "/forgotten-password",
						"/user/resetPassword*", "/user/changePassword*", "/emailError*")
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/user/savePassword*", "/updatePassword*")
				.hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
				.and()
			.authorizeRequests()
				.antMatchers("/user/updatePassword*", "/changePassword")
				.fullyAuthenticated()
				.and()
			.authorizeRequests()
				.antMatchers("/admin/**").hasRole("COMMITTEE_LEADER")
				.antMatchers("/member/**").hasRole("ISAG_MEMBER")
				.antMatchers("/document/**").hasRole("ISAG_MEMBER")
				.antMatchers("/market/**").hasAnyRole("ISAG_MEMBER", "ISAG_ASSOCIATE")
				.and()
			.formLogin()
				.permitAll()
				.loginPage("/login")
				.defaultSuccessUrl("/index")
				.failureUrl("/login.html?error=true")
				.and()
			.exceptionHandling()
				.accessDeniedPage("/page_403")
				.and()
			.logout()
				.permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/index")
				.deleteCookies("JSESSIONID", "CSRF-TOKEN")
				.and()
			.sessionManagement()
				.maximumSessions(1)
				.expiredUrl("/login");
	}

	@Bean
	@Autowired
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		log.info("Authenticating user...");
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
}
