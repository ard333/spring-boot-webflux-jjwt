/*
 * Ardiansyah | http://ard.web.id
 * 
 */
package id.web.ard.springbootwebfluxjjwt.security;

import id.web.ard.springbootwebfluxjjwt.security.model.Role;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

/**
 *
 * @author ardiansyah
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		
		String username = null;
		try {
			username = jwtUtil.getUsernameFromToken(authToken);
		} catch (Exception e) {
			username = null;
		}
		if (username != null) {
			Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
			if (jwtUtil.validateToken(authToken)) {
				List<String> rolesMap = claims.get("role", List.class);
				List<Role> roles = new ArrayList<>();
				for (String rolemap : rolesMap) {
					roles.add(Role.valueOf(rolemap));
				}
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					username,
					null,
					roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
				);
				return Mono.just(auth);
			} else {
				return Mono.empty();
			}
		} else {
			return Mono.empty();
		}
	}
}
