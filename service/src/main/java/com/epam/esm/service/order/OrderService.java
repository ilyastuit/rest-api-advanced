package com.epam.esm.service.order;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderStatus;
import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserDTO;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.UserNotFoundException;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import com.epam.esm.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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

        MapSqlParameterSource params = prepareUpdate(user, giftCertificate);
        params.addValue("order_date", Timestamp.valueOf(LocalDateTime.now()));

        orderRepository.orderCertificateForUser(params);
    }

    public Page<Order> getOrdersOfUser(int userId, Pageable pageable) {
        return orderRepository.findOrdersOfUser(userId, pageable);
    }

    private MapSqlParameterSource prepareUpdate(User user, GiftCertificate giftCertificateDTO) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("price", giftCertificateDTO.getPrice());
        params.addValue("status", OrderStatus.UNPAID.getValue());
        params.addValue("user_id", user.getId());
        params.addValue("gift_certificate_id", giftCertificateDTO.getId());
        params.addValue("last_update_date", Timestamp.valueOf(LocalDateTime.now()));

        return params;
    }
}
