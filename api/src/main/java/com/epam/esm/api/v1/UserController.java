package com.epam.esm.api.v1;

import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderDTO;
import com.epam.esm.entity.user.UserDTO;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.UserNotFoundException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> all() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> one(@PathVariable("userId") int userId) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getOne(userId), HttpStatus.OK);
    }

    @PutMapping("/{userId}/order/{giftCertificateId}")
    public void orderGiftCertificate(@PathVariable int userId, @PathVariable int giftCertificateId) throws UserNotFoundException, GiftCertificateNotFoundException {
        orderService.orderCertificateForUser(userId, giftCertificateId);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable int userId) {
        return new ResponseEntity<>(orderService.getOrdersOfUser(userId), HttpStatus.OK);
    }
}
