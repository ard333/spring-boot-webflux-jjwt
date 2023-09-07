package com.ard333.springbootwebfluxjjwt.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import com.ard333.springbootwebfluxjjwt.model.security.AuthResponse;
import com.ard333.springbootwebfluxjjwt.security.model.AuthRequest;
import com.ard333.springbootwebfluxjjwt.utils.BaseRestTest;

public class LoginTest extends BaseRestTest {

	@Test
	public void whenLoginWithBadPassword_shouldRespondWith4xx() {

		webClient()
				.post()
				.uri("/login")
				.bodyValue(AuthRequest.builder()
						.username("user")
						.password("wrong")
						.build())
				.exchangeToMono(res -> {
					assertThat(res.statusCode()).isEqualTo(HttpStatusCode.valueOf(401));
					return res.bodyToMono(AuthRequest.class);
				})
				.doOnError(e -> Assertions.fail("should not throw", e))
				.block();

	}

	@Test
	public void whenLoginWithUser_shouldRespondWith2xx() {
		webClient()
				.post()
				.uri("/login")
				.bodyValue(AuthRequest.builder()
						.username("user")
						.password("user")
						.build())
				.exchangeToMono(res -> {

					assertThat(res.statusCode().is2xxSuccessful()).isTrue();
					return res.bodyToMono(AuthResponse.class);

				})
				.doOnError(e -> Assertions.fail("should not throw", e))
				.doOnSuccess(authRes -> {
					assertThat(authRes.getToken()).isNotEmpty();
				})
				.block();
	}
}
