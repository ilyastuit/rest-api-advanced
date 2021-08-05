package com.epam.esm.repository.user;

import com.epam.esm.entity.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getTimestamp("create_date").toLocalDateTime(),
                    resultSet.getTimestamp("last_update_date").toLocalDateTime(),
                    null
            );

            users.add(user);
        }
        return users;
    }
}
