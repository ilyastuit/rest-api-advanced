package com.epam.esm.repository.user;

import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String SQL = "SELECT * from users.list";
        return jdbcTemplate.query(SQL, new UserResultSetExtractor());
    }

    public List<User> findById(int userId) {
        String SQL = "SELECT * from users.list where id = ?";
        return jdbcTemplate.query(SQL, new UserResultSetExtractor(), userId);
    }
}
