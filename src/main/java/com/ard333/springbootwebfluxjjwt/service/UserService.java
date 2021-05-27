package com.ard333.springbootwebfluxjjwt.service;

import com.ard333.springbootwebfluxjjwt.model.User;
import com.ard333.springbootwebfluxjjwt.security.model.Role;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 *
 * @author ard333
 */
@Service
public class UserService {
    // this is just an example, you can load the user from the database from the repository

    private Map<String, User> data;
    @PostConstruct
    public void init(){
        data = new HashMap<>();
            //username:passwowrd -> user:user
        data.put("user", new User("user", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true, Arrays.asList(Role.ROLE_USER)));

        //username:passwowrd -> admin:admin
        data.put("admin", new User("admin", "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", true, Arrays.asList(Role.ROLE_ADMIN)));
    }
    public Mono<User> findByUsername(String username) {
        if (data.containsKey(username)) {
            return Mono.just(data.get(username));
        } else {
            return Mono.empty();
        }
    }
}
