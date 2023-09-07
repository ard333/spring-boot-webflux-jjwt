package com.ard333.springbootwebfluxjjwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ard333.springbootwebfluxjjwt.rest.AuthenticationREST;
import com.ard333.springbootwebfluxjjwt.rest.ResourceREST;

@SpringBootTest
class SpringBootWebfluxJjwtApplicationTest {

    @Autowired
    AuthenticationREST authenticationREST;

    @Autowired
    ResourceREST resourceREST;

    @Test
    void contextLoads() {
        Assertions.assertThat(authenticationREST).isNotNull();
        Assertions.assertThat(resourceREST).isNotNull();
    }

}
