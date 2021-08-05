package com.epam.esm.repository.order;

import com.epam.esm.entity.order.Order;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    public OrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate, UserRepository userRepository, GiftCertificateRepository giftCertificateRepository) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    public void orderCertificateForUser(SqlParameterSource params) {
        final String SQL = "INSERT INTO orders.gift_certificate (price, status, user_id, gift_certificate_id, order_date, last_update_date) VALUES (:price, :status::orders.order_status, :user_id, :gift_certificate_id, :order_date, :last_update_date)";
        namedParameterJdbcTemplate.update(SQL, params);
    }

    public Page<Order> findOrdersOfUser(int userId, Pageable pageable) {

        StringBuilder builder = new StringBuilder("SELECT * FROM orders.gift_certificate ogc LEFT JOIN users.list u ON u.id = ogc.user_id WHERE u.id = ? ");

        final String SQL = getPageableStatement(builder, pageable);
        long count = countOrdersOfUser(userId);

        return new PageImpl<>(
                jdbcTemplate.query(SQL, new OrderResultSetExtractor(userRepository, giftCertificateRepository), userId),
                pageable,
                (int) count);
    }

    public int countOrdersOfUser(int userId) {
        return jdbcTemplate.queryForObject("SELECT count(ogc.id) FROM orders.gift_certificate ogc LEFT JOIN users.list u ON u.id = ogc.user_id WHERE u.id = ?", Integer.class, userId);
    }

    private String getPageableStatement(StringBuilder SQL, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            SQL.append(" ORDER BY ogc.")
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
