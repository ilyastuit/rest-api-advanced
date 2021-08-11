package com.epam.esm.service.order;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderStatus;
import com.epam.esm.entity.user.User;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.UserNotFoundException;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import com.epam.esm.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;

    public OrderService(OrderRepository orderRepository, UserService userService, GiftCertificateService giftCertificateService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
    }

    public void orderCertificateForUser(int userId, int giftCertificateId) throws UserNotFoundException, GiftCertificateNotFoundException {
        User user = userService.getOne(userId);
        GiftCertificate giftCertificate = giftCertificateService.getOne(giftCertificateId, false);

        Order order = new Order(
                giftCertificate.getPrice(),
                OrderStatus.UNPAID,
                LocalDateTime.now(),
                LocalDateTime.now(),
                user,
                giftCertificate
        );

        orderRepository.save(order);
    }

    public Page<Order> getOrdersOfUser(int userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }
}
