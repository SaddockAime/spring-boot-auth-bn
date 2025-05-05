// src/main/java/com/africahr/auth/dto/UserDTO.java

package com.africahr.auth.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private Set<String> roles;
}