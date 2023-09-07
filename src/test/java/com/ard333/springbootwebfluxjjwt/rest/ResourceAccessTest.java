package com.ard333.springbootwebfluxjjwt.rest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.ard333.springbootwebfluxjjwt.model.Message;
import com.ard333.springbootwebfluxjjwt.model.security.AuthResponse;
import com.ard333.springbootwebfluxjjwt.utils.BaseRestTest;

public class ResourceAccessTest extends BaseRestTest {
	private static String REQ_MAPPING = "/resource";
	private static String USER_URL = REQ_MAPPING + "/user";
	private static String ADMIN_URL = REQ_MAPPING + "/admin";
	private static String USER_OR_ADMIN_URL = REQ_MAPPING + "/user-or-admin";

	@Test
	public void whenInvalidToken_shouldRespondWith401() {

		webClient()
				.get()
				.uri(USER_URL)
				.exchangeToMono(res -> {
					assertTrue(res.statusCode().is4xxClientError());
					return res.bodyToMono(AuthResponse.class);
				})
				.doOnError(e -> fail("error on test", e))
				.block();
	}

	@Test
	public void whenTokenValid_and_userHasAccess_responseShouldBe2xx() {

		userWebClient()
				.transform(m -> m.flatMap(c -> c.get()
						.uri(USER_URL)
						.exchangeToMono(res -> {
							assertTrue(res.statusCode().is2xxSuccessful());
							return res.bodyToMono(Message.class);
						})))
				.doOnError(e -> fail("error on test", e))
				.block();
	}

	@Test
	public void whenTokenValid_and_doNotHasAccess_responseShouldBe4xx() {

		userWebClient()
				.transform(m -> m.flatMap(c -> c.get()
						.uri(ADMIN_URL)
						.exchangeToMono(res -> {
							assertTrue(res.statusCode().is4xxClientError());
							return res.bodyToMono(Message.class);
						})))
				.doOnError(e -> fail("error on test", e))
				.block();
	}

	@Test
	public void whenTokenValid_and_adminHasAccess_responseShouldBe2xx() {

		adminWebClient()
				.transform(m -> m.flatMap(c -> c.get()
						.uri(ADMIN_URL)
						.exchangeToMono(res -> {
							assertTrue(res.statusCode().is2xxSuccessful());
							return res.bodyToMono(Message.class);
						})))
				.doOnError(e -> fail("error on test", e))
				.block();
	}

	@Test
	public void whenTokenValid_and_adminAndUserHasAccess_responseShouldBe2xx() {

		adminWebClient()
				.transform(m -> m.flatMap(c -> c.get()
						.uri(USER_OR_ADMIN_URL)
						.exchangeToMono(res -> {
							assertTrue(res.statusCode().is2xxSuccessful());
							return res.bodyToMono(Message.class);
						})))
				.doOnError(e -> fail("error on test", e))
				.block();

		userWebClient()
				.transform(m -> m.flatMap(c -> c.get()
						.uri(USER_OR_ADMIN_URL)
						.exchangeToMono(res -> {
							assertTrue(res.statusCode().is2xxSuccessful());
							return res.bodyToMono(Message.class);
						})))
				.doOnError(e -> fail("error on test", e))
				.block();
	}

}
