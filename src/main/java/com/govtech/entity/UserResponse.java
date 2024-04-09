package com.govtech.entity;

import java.util.Set;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserResponse {
	
    private String token;
    
    private String refreshToken;
    
    private Set<String> roles;
    
    private String message;

}
