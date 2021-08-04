package com.epam.esm.repository.user;

import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final UserResultSetExtractor userResultSetExtractor = new UserResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Page<User> findAll(Pageable pageable) {
        StringBuilder builder = new StringBuilder("SELECT u.* from users.list u");

        final String SQL = getPageableStatement(builder, pageable);
        long count = countAll();

        return new PageImpl<>(
                jdbcTemplate.query(SQL, userResultSetExtractor),
                pageable,
                (int) count);
    }

    public int countAll() {
        return jdbcTemplate.queryForObject("SELECT count(u.id) from users.list u ", Integer.class);
    }

    public List<User> findById(int userId) {
        String SQL = "SELECT * from users.list where id = ?";
        return jdbcTemplate.query(SQL, userResultSetExtractor, userId);
    }

    private String getPageableStatement(StringBuilder SQL, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            SQL.append(" ORDER BY u.")
                    .append(o.getProperty())
                    .append(" ")
                    .append(o.getDirection()).append(" ");
        }
        int offset = Math.max(pageable.getPageNumber() - 1, 0);
        SQL.append(" OFFSET ")
                .append(offset * pageable.getPageSize())
                .append("ROWS ")
                .append("FETCH NEXT ")
                .append(pageable.getPageSize())
                .append("ROWS ONLY");

        return SQL.toString();
    }
}
