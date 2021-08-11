package com.epam.esm.repository.order;

import com.epam.esm.TestEnvironment;
import com.epam.esm.builder.GiftCertificateBuilder;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderStatus;
import com.epam.esm.TestRepositoryConfig;
import com.epam.esm.entity.user.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TestRepositoryConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private final GiftCertificateBuilder certificateBuilder = new GiftCertificateBuilder();

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        Flyway flyway = TestEnvironment.getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void successFindOrdersOfUser() {
        Order fetchedOrder = orderRepository.findByUserId(4, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).stream().findFirst().get();

        assertEquals(fetchedOrder.getGiftCertificate().getId(), 5);
        assertEquals(fetchedOrder.getStatus(), OrderStatus.DONE);
    }

    @Test
    void successOrder() {
        assertEquals(0, orderRepository.findByUserId(5, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).getTotalElements());

        GiftCertificate giftCertificate =certificateBuilder.build();

        Order order = new Order(
                BigDecimal.valueOf(10),
                OrderStatus.UNPAID,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new User(5, "admin@mail.ru", "123", Timestamp.valueOf("2020-10-09 10:48:23").toLocalDateTime(), Timestamp.valueOf("2020-10-09 10:48:23").toLocalDateTime(), null),
                giftCertificate
        );

        orderRepository.save(order);

        Order fetchedOrder = orderRepository.findByUserId(5, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))).stream().findFirst().get();

        assertEquals(fetchedOrder.getGiftCertificate().getId(), giftCertificate.getId());
        assertEquals(fetchedOrder.getStatus(), OrderStatus.UNPAID);
    }
}
