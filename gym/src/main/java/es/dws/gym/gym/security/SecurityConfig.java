package es.dws.gym.gym.security;

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

		return new BCryptPasswordEncoder();
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
					//USERS
					.requestMatchers(HttpMethod.GET, "/api/users/*/").hasRole("USER")
					.requestMatchers(HttpMethod.PUT, "/api/users/*/").hasRole("USER")
					.requestMatchers(HttpMethod.PUT, "/api/users/*/image/").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/users/*/image/").hasRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/users/*/").hasRole("ADMIN")
					//REVIEW
					.requestMatchers(HttpMethod.POST, "/api/review").hasAnyRole("USER")
					.requestMatchers(HttpMethod.PUT, "/api/review").hasAnyRole("USER")
					.requestMatchers(HttpMethod.DELETE, "/api/review/**").hasAnyRole("USER")
					//GYMCLASS
					.requestMatchers(HttpMethod.POST, "/api/gymclass").hasAnyRole("ADMIN")
					.requestMatchers(HttpMethod.PUT, "/api/gymclass").hasAnyRole("ADMIN")
					.requestMatchers(HttpMethod.DELETE, "/api/gymclass/**").hasAnyRole("ADMIN")
					// PUBLIC ENDPOINTS
					.requestMatchers(HttpMethod.POST, "/api/users/").permitAll() //register
					.requestMatchers(HttpMethod.GET, "/api/review/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/gymclass/**").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
//					.requestMatchers(HttpMethod.GET, "/api/users/").permitAll() //if not conflict vs webFilterChain
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
