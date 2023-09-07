package com.ard333.springbootwebfluxjjwt.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ard333.springbootwebfluxjjwt.model.Message;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/resource")
public class ResourceREST {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<Message>> user() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user")));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Message>> admin() {
        return Mono.just(ResponseEntity.ok(new Message("Content for admin")));
    }

    @GetMapping("/user-or-admin")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Message>> userOrAdmin() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user or admin")));
    }
}
