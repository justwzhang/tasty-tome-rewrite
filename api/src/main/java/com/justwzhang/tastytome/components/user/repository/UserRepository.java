package com.justwzhang.tastytome.components.user.repository;

import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.justwzhang.tastytome.components.user.model.User;
import static com.justwzhang.tastytome.jooq.generated.tables.User.USER;

@Repository
public class UserRepository {

    private final DSLContext dsl;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(
            dsl.selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .fetchOneInto(User.class));
    }

    public User createIfMissingByEmail(
        String email,
        String firstName,
        String lastName
    ) {
        User createdUser = dsl.insertInto(USER)
            .set(USER.EMAIL, email)
            .set(USER.FIRST_NAME, firstName)
            .set(USER.LAST_NAME, lastName)
            .onConflict(USER.EMAIL)
            .doNothing()
            .returning()
            .fetchOneInto(User.class);

        if (createdUser != null) {
            return createdUser;
        }

        return findByEmail(email).orElseThrow(
            () -> new IllegalStateException("User creation conflicted but no user was found"));
    }
}