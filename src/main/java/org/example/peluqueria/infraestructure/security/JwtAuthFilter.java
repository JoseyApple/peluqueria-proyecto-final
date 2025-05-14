package org.example.peluqueria.infraestructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final AppUserDetailService appUserDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		String method = request.getMethod();

		// Sólo saltar CORS y rutas públicas de login/h2
		if (CorsUtils.isPreFlightRequest(request)
				|| path.startsWith("/auth/")
				|| path.startsWith("/h2-console/")
				|| (path.startsWith("/users") && "POST".equalsIgnoreCase(method))) {
			filterChain.doFilter(request, response);
			return;
		}
		// A PARTIR DE AQUÍ, procesar TODOS los métodos, incluidos GET
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
			return;
		}

		String token = authHeader.substring(7);
		try {
			String email = jwtService.extractUsername(token);

			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = appUserDetailService.loadUserByUsername(email);

				if (!jwtService.isValidToken(token, userDetails)) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
					return;
				}

				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException eje) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
		} catch (JwtException | IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
		}
	}

}