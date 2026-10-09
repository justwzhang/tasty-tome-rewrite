package com.justwzhang.tastytome.components.user.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.justwzhang.tastytome.components.user.model.User;
import com.justwzhang.tastytome.components.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateCurrentUser(Jwt jwt) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required");
        }

        String email = requiredClaim(jwt, "email");
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user = existingUser.orElseGet(() -> createUserFromNameClaim(jwt, email));
        return user;
    }

    private User createUserFromNameClaim(Jwt jwt, String email) {
        String fullName = requiredClaim(jwt, "name").trim();
        String[] nameParts = fullName.split("\\s+", 2);
        if (nameParts.length != 2 || nameParts[1].isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "JWT 'name' claim must contain first and last names separated by a space");
        }

        return userRepository.createIfMissingByEmail(email, nameParts[0], nameParts[1].trim());
    }

    private String requiredClaim(Jwt jwt, String claimName) {
        String value = jwt.getClaimAsString(claimName);
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "JWT must contain a nonblank '" + claimName + "' claim");
        }
        return value;
    }
}