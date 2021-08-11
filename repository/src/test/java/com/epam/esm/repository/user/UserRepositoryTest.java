package com.epam.esm.repository.user;

import com.epam.esm.TestRepositoryConfig;
import com.epam.esm.builder.UserBuilder;
import com.epam.esm.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.epam.esm.builder.UserBuilder.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = TestRepositoryConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

    private final UserBuilder builder = new UserBuilder();

    @Autowired
    private UserRepository repository;

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
        assertNull(repository.findById(NOT_EXIST_USER_ID));
    }

    private void assertUsers(User originalUser, User fetchedUser) {
        assertEquals(originalUser.getId(), fetchedUser.getId());
        assertEquals(originalUser.getEmail(), fetchedUser.getEmail());
        assertEquals(originalUser.getPassword(), fetchedUser.getPassword());
        assertEquals(originalUser.getCreateDate(), fetchedUser.getCreateDate());
        assertEquals(originalUser.getLastUpdateDate(), fetchedUser.getLastUpdateDate());
    }
}
