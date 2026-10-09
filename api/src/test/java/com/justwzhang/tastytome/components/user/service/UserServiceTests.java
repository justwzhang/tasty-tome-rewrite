package com.justwzhang.tastytome.components.user.service;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import com.justwzhang.tastytome.components.user.model.User;
import com.justwzhang.tastytome.components.user.repository.UserRepository;

class UserServiceTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void returnsAnExistingUserUsingOnlyTheJwtEmail() {
        Jwt jwt = jwt("person@example.com", null);
        User user = mock(User.class);
        when(userRepository.findByEmail("person@example.com")).thenReturn(Optional.of(user));

        User response = userService.getOrCreateCurrentUser(jwt);

        assertSame(user, response);
        verify(userRepository).findByEmail("person@example.com");
    }

    @Test
    void createsAUserWhenEmailIsNotFound() {
        Jwt jwt = jwt("person@example.com", "Justin Zhang Lee");
        User user = mock(User.class);
        when(userRepository.findByEmail("person@example.com")).thenReturn(Optional.empty());
        when(userRepository.createIfMissingByEmail("person@example.com", "Justin", "Zhang Lee"))
            .thenReturn(user);

        User response = userService.getOrCreateCurrentUser(jwt);

        assertSame(user, response);
        verify(userRepository).createIfMissingByEmail("person@example.com", "Justin", "Zhang Lee");
    }

    @Test
    void rejectsMissingOrBlankEmailBeforeRepositoryAccess() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> userService.getOrCreateCurrentUser(jwt(" ", "Taylor Person")));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    void rejectsNameClaimWithoutAFirstAndLastNameBeforeCreatingUser() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> userService.getOrCreateCurrentUser(jwt("person@example.com", "Justin")));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(userRepository).findByEmail("person@example.com");
    }

    private Jwt jwt(String email, String name) {
        Jwt.Builder builder = Jwt.withTokenValue("test-token")
            .header("alg", "none")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(60));
        if (email != null) {
            builder.claim("email", email);
        }
        if (name != null) {
            builder.claim("name", name);
        }
        return builder.build();
    }
}