package org.example.peluqueria.infraestructure.security;

import lombok.AllArgsConstructor;
import org.example.peluqueria.exceptions.UserUnauthorizedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

	private final UserDetailsService userDetailsService;
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/h2-console/**", "/auth/**").permitAll() // Login, consola H2 públicas

						.requestMatchers(HttpMethod.GET, "/services/**").permitAll() // Cualquier usuario puede ver servicios

						.requestMatchers(HttpMethod.GET, "/appointments/client/**").hasAnyRole("USER", "ADMIN") // Ver sus citas
						.requestMatchers(HttpMethod.POST, "/appointments").hasAnyRole("USER", "ADMIN") // Crear cita
						.requestMatchers(HttpMethod.DELETE, "/appointments/**").hasAnyRole("USER", "ADMIN") // Cancelar cita

						.requestMatchers(HttpMethod.GET, "/appointments").hasRole("ADMIN") // Listar todas las citas → solo admin

						.requestMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("USER", "ADMIN") // Crear order
						.requestMatchers(HttpMethod.PATCH, "/orders/**").hasAnyRole("USER", "ADMIN") // Pagar order
						.requestMatchers(HttpMethod.GET, "/orders/client/**").hasAnyRole("USER", "ADMIN") // Ver orders

						.anyRequest().hasRole("ADMIN") // Por defecto, solo admin
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(this::configureExceptionHandling);

		return http.build();
	}

	private void configureExceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling) {
		exceptionHandling
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					UserUnauthorizedException invalidTokenException = new UserUnauthorizedException("Access denied: You don't have permission to access this resource");
					handlerExceptionResolver.resolveException(request, response, null, invalidTokenException);
				})
				.authenticationEntryPoint((request, response, authException) -> {
					UserUnauthorizedException invalidTokenException = new UserUnauthorizedException("Access denied: Invalid token");
					handlerExceptionResolver.resolveException(request, response, null, invalidTokenException);
				});
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(authenticationProvider());
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
