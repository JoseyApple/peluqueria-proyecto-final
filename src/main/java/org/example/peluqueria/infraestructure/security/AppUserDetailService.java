package org.example.peluqueria.infraestructure.security;

import lombok.AllArgsConstructor;
import org.example.peluqueria.domain.Role;
import org.example.peluqueria.domain.models.AppUser;
import org.example.peluqueria.infraestructure.repositories.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserDetailService implements UserDetailsService {

	private final AppUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User with this email does not exist: " + email));

		// Convertir Role enum a GrantedAuthority
		List<GrantedAuthority> authorities = List.of(
				new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
		);

		// Devolver tu UserPrincipal con el ID incluido
		return new UserPrincipal(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				authorities
		);
	}

	private String[] extractRoles(Role role) {
		if (role == null) {
			return new String[]{Role.USER.name()};
		}
		return new String[]{role.name()};
	}
}
