/*
 * Ardiansyah | http://ard.web.id
 * 
 */
package id.web.ard.springbootwebfluxjjwt.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author ardiansyah
 */
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class AuthResponse {
	
	private String token;

}
