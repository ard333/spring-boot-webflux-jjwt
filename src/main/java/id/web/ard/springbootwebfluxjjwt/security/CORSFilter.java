/*
 * Ardiansyah | http://ard.web.id
 * 
 */
package id.web.ard.springbootwebfluxjjwt.security;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 *
 * @author ardiansyah
 */
@Component
public class CORSFilter implements WebFilter{

	@Override
	public Mono<Void> filter(ServerWebExchange swe, WebFilterChain wfc) {
		
		//CORS
		swe.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
		if (swe.getRequest().getHeaders().get("Access-Control-Request-Method") != null && "OPTIONS".equalsIgnoreCase(swe.getRequest().getMethod().toString())) {
			swe.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Authorization");
			swe.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Content-Type");
			swe.getResponse().getHeaders().add("Access-Control-Max-Age", "1");
			swe.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		}
		
        return wfc.filter(swe);
	}

}
