package com.justwzhang.tastytome.components.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import com.justwzhang.tastytome.components.user.model.UserResponse;
import com.justwzhang.tastytome.components.user.repository.UserRepository;
import com.justwzhang.tastytome.jooq.generated.tables.pojos.User;

class UserServiceTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void returnsAnExistingUserUsingOnlyTheJwtEmail() {
        Jwt jwt = jwt("person@example.com", null, null);
        User user = new User()
            .setUserId(42L)
            .setFirstName("Taylor")
            .setLastName("Person")
            .setEmail("person@example.com");
        when(userRepository.findByEmail("person@example.com")).thenReturn(Optional.of(user));

        UserResponse response = userService.getOrCreateCurrentUser(jwt);

        assertEquals(new UserResponse(42L, "Taylor", "Person", "person@example.com"), response);
        verify(userRepository).findByEmail("person@example.com");
    }

    @Test
    void createsAUserWhenEmailIsNotFound() {
        Jwt jwt = jwt("person@example.com", "Taylor", "Person");
        User user = new User()
            .setUserId(42L)
            .setFirstName("Taylor")
            .setLastName("Person")
            .setEmail("person@example.com");
        when(userRepository.findByEmail("person@example.com")).thenReturn(Optional.empty());
        when(userRepository.createIfMissingByEmail("person@example.com", "Taylor", "Person"))
            .thenReturn(user);

        UserResponse response = userService.getOrCreateCurrentUser(jwt);

        assertEquals(new UserResponse(42L, "Taylor", "Person", "person@example.com"), response);
        verify(userRepository).createIfMissingByEmail("person@example.com", "Taylor", "Person");
    }

    @Test
    void rejectsMissingOrBlankEmailBeforeRepositoryAccess() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> userService.getOrCreateCurrentUser(jwt(" ", "Taylor", "Person")));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    void rejectsMissingNameClaimsBeforeRepositoryAccess() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> userService.getOrCreateCurrentUser(jwt("person@example.com", "Taylor", null)));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(userRepository).findByEmail("person@example.com");
    }

    private Jwt jwt(String email, String givenName, String familyName) {
        Jwt.Builder builder = Jwt.withTokenValue("test-token")
            .header("alg", "none")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(60));
        if (email != null) {
            builder.claim("email", email);
        }
        if (givenName != null) {
            builder.claim("given_name", givenName);
        }
        if (familyName != null) {
            builder.claim("family_name", familyName);
        }
        return builder.build();
    }
}