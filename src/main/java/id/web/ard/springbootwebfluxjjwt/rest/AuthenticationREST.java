/*
 * Ardiansyah | http://ard.web.id
 * 
 */
package id.web.ard.springbootwebfluxjjwt.rest;

import id.web.ard.springbootwebfluxjjwt.security.JWTUtil;
import id.web.ard.springbootwebfluxjjwt.security.PBKDF2Encoder;
import id.web.ard.springbootwebfluxjjwt.security.model.AuthRequest;
import id.web.ard.springbootwebfluxjjwt.security.model.AuthResponse;
import id.web.ard.springbootwebfluxjjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author ardiansyah
 */
@RestController
public class AuthenticationREST {

	@Autowired
	private JWTUtil jwtTokenUtil;
	
	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private UserService userRepository;

	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public Mono<ResponseEntity<AuthResponse>> auth(@RequestBody AuthRequest ar) {
		return userRepository.findByUsername(ar.getUsername()).map((userDetails) -> {
			if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
				return ResponseEntity.ok(new AuthResponse(jwtTokenUtil.generateToken(userDetails)));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		});
	}

}
