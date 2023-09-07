package com.ard333.springbootwebfluxjjwt.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author ard333
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class AuthRequest {
    private String username;
    private String password;

}
