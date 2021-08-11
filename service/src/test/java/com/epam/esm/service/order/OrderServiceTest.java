package com.epam.esm.service.order;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.user.User;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.UserNotFoundException;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import com.epam.esm.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    public OrderRepository orderRepository;
    @Mock
    public UserService userService;
    @Mock
    public GiftCertificateService giftCertificateService;

    public OrderService orderService;

    @BeforeEach
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, userService, giftCertificateService);
    }

    @Test
    void successOrderCertificateForUser() throws UserNotFoundException, GiftCertificateNotFoundException {
        User user = new User();
        GiftCertificate giftCertificate = new GiftCertificate();

        when(userService.getOne(1)).thenReturn(user);
        when(giftCertificateService.getOne(1, false)).thenReturn(giftCertificate);
        when(orderRepository.save(any(Order.class))).thenReturn(any(Order.class));

        orderService.orderCertificateForUser(1, 1);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(userService, times(1)).getOne(1);
        verify(giftCertificateService, times(1)).getOne(1, false);
    }

    @Test
    void successGetOrderOfUser() {
        Pageable pageable = Pageable.unpaged();
        Page<Order> page = new PageImpl<>(Collections.emptyList());

        when(orderRepository.findByUserId(1, pageable)).thenReturn(page);

        orderService.getOrdersOfUser(1, pageable);

        verify(orderRepository, times(1)).findByUserId(1, pageable);
    }
}
