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
        User user = existingUser.orElseGet(() -> userRepository.createIfMissingByEmail(
            email,
            requiredClaim(jwt, "given_name"),
            requiredClaim(jwt, "family_name")));
        return user;
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