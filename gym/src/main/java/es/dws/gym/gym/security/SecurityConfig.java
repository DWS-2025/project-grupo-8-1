package es.dws.gym.gym.security;

import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.dws.gym.gym.security.jwt.JwtRequestFilter;
import es.dws.gym.gym.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtRequestFilter JwtRequestFilter;

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom());
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

    @Bean
	static RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.withDefaultRolePrefix()
				.role("ADMIN").implies("USER")
				.role("USER").implies("ANONYMOUS")
                .build();
	}

	@Bean
	static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
    	
	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy());

		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS
					 // USERS
					.requestMatchers(HttpMethod.GET, "/api/user").hasRole("ADMIN")
					.requestMatchers(HttpMethod.POST, "/api/user").permitAll()
					.requestMatchers(HttpMethod.PUT, "/api/user/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/user/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/user/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/user/image/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.PUT, "/api/user/image/{id}").hasRole("USER")
					// REVIEW
					.requestMatchers(HttpMethod.GET, "/api/review").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/review/paginated").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/review/{id}").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/review").hasRole("USER")
					.requestMatchers(HttpMethod.PUT, "/api/review").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/review/{id}").hasRole("USER")
					// GYMCLASS
					.requestMatchers(HttpMethod.GET, "/api/gymclass").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/gymclass/{id}").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/gymclass").hasRole("ADMIN") 
					.requestMatchers(HttpMethod.PUT, "/api/gymclass/{id}").hasRole("ADMIN") 
					.requestMatchers(HttpMethod.DELETE, "/api/gymclass/{id}").hasRole("ADMIN") 
					.requestMatchers(HttpMethod.POST, "/api/gymclass/toggle/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/gymclass/toggle/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/gymclass/pdf/{id}").hasRole("USER")
					.requestMatchers(HttpMethod.POST, "/api/gymclass/pdf/{id}").hasRole("ADMIN") 
					// LOGIN
					.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/auth/logout").hasAnyRole("USER", "ADMIN")
			);
		
        // Disable Form login Authentication
        http.formLogin(AbstractHttpConfigurer::disable);

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(AbstractHttpConfigurer::disable);

        // Disable Basic Authentication
        http.httpBasic(AbstractHttpConfigurer::disable);

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(JwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						 // ANONYMOUS PAGES
						.requestMatchers("/login").anonymous()
						.requestMatchers("/login/error").anonymous()
						.requestMatchers("/register").anonymous()
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/images/**").permitAll()
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/js/**").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/review").permitAll()
						.requestMatchers("/gymclass").permitAll()
						.requestMatchers("/gymclass/search**").permitAll()
						.requestMatchers("/fonts/**").permitAll()
						// PRIVATE PAGES
						.requestMatchers("/gymclass/{id:[0-9]+}/toggleUser").hasAnyRole("USER")
						.requestMatchers("/gymclass/file/{id:[0-9]+}").hasAnyRole("USER")
						.requestMatchers("/gymclass/**").hasAnyRole("ADMIN")
						.requestMatchers("/admin/**").hasAnyRole("ADMIN")
						.requestMatchers("/home").hasRole("USER")
						.requestMatchers("/review/**").hasAnyRole("USER")
						.requestMatchers("/user/**").hasAnyRole("USER")
				)
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/login/error")
						.defaultSuccessUrl("/home")
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll()
				)
				.csrf(csrf->csrf.disable());
				
		// Require HTTPS for all web requests
		http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
				
		return http.build();
	}
}
