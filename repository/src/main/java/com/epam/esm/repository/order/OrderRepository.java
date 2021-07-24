package com.epam.esm.repository.order;

import com.epam.esm.entity.order.Order;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.repository.user.UserRepository;
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

    public List<Order> findOrdersOfUser(int userId) {
        final String SQL = "SELECT * FROM orders.gift_certificate ogc LEFT JOIN users.list u ON u.id = ogc.user_id WHERE u.id = ?";
        return jdbcTemplate.query(SQL, new OrderResultSetExtractor(userRepository, giftCertificateRepository), userId);
    }
}
