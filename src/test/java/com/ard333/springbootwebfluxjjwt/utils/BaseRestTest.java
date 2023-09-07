package com.ard333.springbootwebfluxjjwt.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.ard333.springbootwebfluxjjwt.model.security.AuthResponse;
import com.ard333.springbootwebfluxjjwt.security.model.AuthRequest;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Configuration
public abstract class BaseRestTest {

	protected static String LOGIN_URL = "/login";

	protected Map<String, String> tokenMap = new HashMap<>();

	@Value(value = "${local.server.port}")
	protected int port;

	@Autowired
	protected TestRestTemplate restTemplate;

	@Autowired
	protected TestRestTemplate userRestTemplate;

	@Bean
	public TestRestTemplate userRestTemplate() {
		RestTemplateBuilder builder = new RestTemplateBuilder(rt -> rt.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("Authorization", new StringBuilder("Bearer ").append(getUserToken())
					.toString());
			return execution.execute(request, body);
		}));
		return new TestRestTemplate(builder);
	}

	public StringBuilder createURL(String resource) {
		return new StringBuilder("http://localhost:")
				.append(port)
				.append(resource);
	}

	protected ResponseEntity<AuthResponse> userLogin() {
		AuthRequest req = AuthRequest.builder()
				.username("user")
				.password("user")
				.build();

		ResponseEntity<AuthResponse> res = restTemplate.postForEntity(
				createURL(LOGIN_URL).toString(),
				req,
				AuthResponse.class);
		return res;
	}

	@SuppressWarnings("null")
	protected String getUserToken() {
		ResponseEntity<AuthResponse> userLogin = userLogin();
		String token = "";
		try {
			token = userLogin.getBody().getToken();
		} catch (Exception e) {
		}
		return token;
	}

	protected HttpEntity<Map<String, String>> getHttpEntyWithToken(String userToken) {
		return new HttpEntity<>(Map.of("Authorization", new StringBuilder("Bearer ").append(userToken).toString()));
	}

	protected WebClient webClient() {
		return WebClient.create("http://localhost:" + port);
	}

	protected Mono<String> getToken(String userName, String password) {
		return Mono.fromSupplier(() -> {
			String result = "";
			if (tokenMap.containsKey(userName)) {
				result = tokenMap.get(userName);
			} else {
				result = authWebClient(userName, password).block().getToken();
				tokenMap.put(userName, result);
			}
			return result;
		});
	}

	protected Mono<WebClient> userWebClient() {
		return getToken("user", "user")
				.map(token -> webClient()
						.mutate()
						.defaultHeader("Authorization",
								new StringBuilder("Bearer ").append(token).toString())
						.build())
				.cache();
	}
	protected Mono<WebClient> adminWebClient() {
		return getToken("admin", "admin")
				.map(token -> webClient()
						.mutate()
						.defaultHeader("Authorization",
								new StringBuilder("Bearer ").append(token).toString())
						.build())
				.cache();
	}

	private Mono<AuthResponse> authWebClient(String userName, String password) {
		return webClient()
				.post()
				.uri("/login")
				.bodyValue(AuthRequest.builder()
						.username(userName)
						.password(password)
						.build())
				.exchangeToMono(res -> {

					assertThat(res.statusCode().is2xxSuccessful()).isTrue();
					return res.bodyToMono(AuthResponse.class);

				})
				.doOnError(e -> Assertions.fail("should not throw", e))
				.doOnSuccess(authRes -> {
					assertThat(authRes.getToken()).isNotEmpty();
				});
	}

}
