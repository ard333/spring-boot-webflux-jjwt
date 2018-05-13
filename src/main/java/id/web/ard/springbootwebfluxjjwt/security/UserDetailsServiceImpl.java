/*
 * Ardiansyah | http://ard.web.id
 * 
 */
package id.web.ard.springbootwebfluxjjwt.security;

import id.web.ard.springbootwebfluxjjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 *
 * @author ardiansyah
 */
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
	
	@Autowired
	private UserService userRepository;
	
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findUserDetailsByUsername(username);
	}
	
}
