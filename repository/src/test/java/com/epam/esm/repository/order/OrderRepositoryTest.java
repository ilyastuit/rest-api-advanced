package com.epam.esm.repository.order;

import com.epam.esm.TestEnvironment;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderStatus;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderRepositoryTest {

    private static OrderRepository orderRepository;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        orderRepository = TestEnvironment.getOrderRepository();
        Flyway flyway = TestEnvironment.getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successFindOrdersOfUser() {
        Order fetchedOrder = orderRepository.findOrdersOfUser(4, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"))).stream().findFirst().get();

        assertEquals(fetchedOrder.getGiftCertificate().getId(), 5);
        assertEquals(fetchedOrder.getStatus(), OrderStatus.DONE);
    }

    @Test
    void successOrder() {
        assertEquals(0, orderRepository.findOrdersOfUser(5, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"))).getTotalElements());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("price", BigDecimal.valueOf(10));
        params.addValue("status", OrderStatus.UNPAID.getValue());
        params.addValue("user_id", 5);
        params.addValue("gift_certificate_id", 3);
        params.addValue("last_update_date", Timestamp.valueOf(LocalDateTime.now()));
        params.addValue("order_date", Timestamp.valueOf(LocalDateTime.now()));

        orderRepository.orderCertificateForUser(params);

        Order fetchedOrder = orderRepository.findOrdersOfUser(5, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"))).stream().findFirst().get();

        assertEquals(fetchedOrder.getGiftCertificate().getId(), 3);
        assertEquals(fetchedOrder.getStatus(), OrderStatus.UNPAID);
    }
}
