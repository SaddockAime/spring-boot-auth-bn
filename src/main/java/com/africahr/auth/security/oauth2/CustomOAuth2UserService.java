// src/main/java/com/africahr/auth/security/oauth2/CustomOAuth2UserService.java

package com.africahr.auth.security.oauth2;

import com.africahr.auth.model.Role;
import com.africahr.auth.model.RoleName;
import com.africahr.auth.model.User;
import com.africahr.auth.repository.RoleRepository;
import com.africahr.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String googleId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String pictureUrl = (String) attributes.get("picture");
        
        User user = findOrCreateUser(googleId, email, firstName, lastName, pictureUrl);
        
        return oAuth2User;
    }
    
    private User findOrCreateUser(String googleId, String email, String firstName, String lastName, String pictureUrl) {
        Optional<User> userOptional = userRepository.findByGoogleId(googleId);
        
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setLastLogin(LocalDateTime.now());
            existingUser.setProfilePicture(pictureUrl);
            return userRepository.save(existingUser);
        }
        
        userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setGoogleId(googleId);
            existingUser.setLastLogin(LocalDateTime.now());
            existingUser.setProfilePicture(pictureUrl);
            return userRepository.save(existingUser);
        }
        
        // Create new user
        User newUser = new User();
        newUser.setGoogleId(googleId);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setProfilePicture(pictureUrl);
        newUser.setActive(true);
        newUser.setLastLogin(LocalDateTime.now());
        
        // Assign default role
        Role staffRole = roleRepository.findByName(RoleName.ROLE_STAFF.name())
                .orElseThrow(() -> new RuntimeException("Error: Role STAFF is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(staffRole);
        newUser.setRoles(roles);
        
        return userRepository.save(newUser);
    }
}