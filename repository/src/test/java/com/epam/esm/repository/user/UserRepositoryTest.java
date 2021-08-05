package com.epam.esm.repository.user;

import com.epam.esm.TestEnvironment;
import com.epam.esm.builder.UserBuilder;
import com.epam.esm.entity.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.epam.esm.builder.UserBuilder.*;

public class UserRepositoryTest {

    private final UserBuilder builder = new UserBuilder();
    private static UserRepository repository;

    @BeforeAll
    private static void beforeAll() throws IOException {
        repository = TestEnvironment.getUserRepository();
    }

    @Test
    void successFindAll() {
        assertEquals(ALL_USERS_COUNT, repository.findAll(PageRequest.of(0, ALL_USERS_COUNT, Sort.by(Sort.Direction.ASC, "email"))).getTotalElements());
    }

    @Test
    void successFindOne() {
        User originalUser = builder.build();

        assertEquals(1, repository.findById(EXIST_USER_ID).size());
        User fetchedUser = repository.findById(EXIST_USER_ID).get(0);

        assertUsers(originalUser, fetchedUser);
    }

    @Test
    void successEmptyFindOne() {
        assertEquals(0, repository.findById(NOT_EXIST_USER_ID).size());
    }

    private void assertUsers(User originalUser, User fetchedUser) {
        assertEquals(originalUser.getId(), fetchedUser.getId());
        assertEquals(originalUser.getEmail(), fetchedUser.getEmail());
        assertEquals(originalUser.getPassword(), fetchedUser.getPassword());
        assertEquals(originalUser.getCreateDate(), fetchedUser.getCreateDate());
        assertEquals(originalUser.getLastUpdateDate(), fetchedUser.getLastUpdateDate());
    }
}
