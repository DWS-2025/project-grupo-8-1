package es.dws.gym.gym.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.dws.gym.gym.model.User;
import es.dws.gym.gym.repository.UserRepository;

@Service
public class RepositoryUserDetailsService implements UserDetailsService {
    @Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

		User user = userRepository.findByid(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Obtener el rol del usuario desde el modelo User
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRolUser());

		return new org.springframework.security.core.userdetails.User(
				user.getId(),
				user.getPassword(),
				List.of(authority) // Convertimos el único rol en una lista inmutable
		);
	}
}
