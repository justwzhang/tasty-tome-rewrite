package com.justwzhang.tastytome.components.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.justwzhang.tastytome.jooq.generated.tables.pojos.User;

@SpringBootTest(properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://auth.example.invalid",
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://auth.example.invalid/jwks"
})
@Transactional
class UserRepositoryIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void repeatedProvisioningWithTheSameEmailReturnsTheExistingUser() {
        String email = "repo-test-" + UUID.randomUUID() + "@example.invalid";

        User first = userRepository.createIfMissingByEmail(email, "Taylor", "Person");
        User second = userRepository.createIfMissingByEmail(email, "Different", "Name");

        assertEquals(first.getUserId(), second.getUserId());
        assertEquals("Taylor", second.getFirstName());
        assertEquals("Person", second.getLastName());
    }
}