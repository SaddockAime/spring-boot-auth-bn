// src/main/java/com/africahr/auth/dto/JwtResponseDTO.java

package com.africahr.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private UserDTO user;
    
    public JwtResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}