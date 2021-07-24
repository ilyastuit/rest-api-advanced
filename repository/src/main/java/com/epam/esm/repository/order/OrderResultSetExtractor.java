package com.epam.esm.repository.order;

import com.epam.esm.entity.exceptions.IllegalOrderStatusValueException;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderStatus;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {

    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    public OrderResultSetExtractor(UserRepository userRepository, GiftCertificateRepository giftCertificateRepository) {
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Order> orders = new ArrayList<>();

        while (resultSet.next()) {
            OrderStatus status;
            try {
                status = OrderStatus.fromValue(resultSet.getString("status"));
            } catch (IllegalOrderStatusValueException exception) {
                throw new TypeMismatchDataAccessException(exception.getMessage());
            }
            Order order = new Order(
                    resultSet.getInt("id"),
                    resultSet.getBigDecimal("price"),
                    status,
                    userRepository.findById(resultSet.getInt("user_id")).get(0),
                    giftCertificateRepository.findById(resultSet.getInt("gift_certificate_id")).get(0),
                    resultSet.getTimestamp("order_date").toLocalDateTime(),
                    resultSet.getTimestamp("last_update_date").toLocalDateTime()
            );

            orders.add(order);
        }

        return orders;
    }
}
