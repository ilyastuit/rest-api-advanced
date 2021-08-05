package com.epam.esm.api.v1;

import com.epam.esm.api.assembler.OrderModelAssembler;
import com.epam.esm.api.assembler.UserModelAssembler;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderModel;
import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserModel;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.UserNotFoundException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final UserModelAssembler userModelAssembler;
    private final OrderModelAssembler orderModelAssembler;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;
    private final PagedResourcesAssembler<Order> orderPagedResourcesAssembler;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public UserController(
            UserService userService,
            OrderService orderService,
            UserModelAssembler userModelAssembler,
            OrderModelAssembler orderModelAssembler,
            PagedResourcesAssembler<User> pagedResourcesAssembler,
            PagedResourcesAssembler<Order> orderPagedResourcesAssembler) {
        this.userService = userService;
        this.orderService = orderService;
        this.userModelAssembler = userModelAssembler;
        this.orderModelAssembler = orderModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.orderPagedResourcesAssembler = orderPagedResourcesAssembler;
    }

    @GetMapping("/")
    public ResponseEntity<PagedModel<UserModel>> all(Pageable pageable) {

        Page<User> userDTOS = this.userService.getAll(pageable);
        PagedModel<UserModel> userModels = pagedResourcesAssembler.toModel(userDTOS, userModelAssembler);

        return new ResponseEntity<>(userModels, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> one(@PathVariable("userId") int userId) throws UserNotFoundException {
        UserModel userModel =  userModelAssembler.toModel(userService.getOne(userId));

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @PutMapping("/{userId}/order/{giftCertificateId}")
    public void orderGiftCertificate(@PathVariable int userId, @PathVariable int giftCertificateId) throws UserNotFoundException, GiftCertificateNotFoundException {
        orderService.orderCertificateForUser(userId, giftCertificateId);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<PagedModel<OrderModel>> getOrders(@PathVariable int userId, Pageable pageable) {

        Page<Order> orders = orderService.getOrdersOfUser(userId, pageable);
        PagedModel<OrderModel> pagedModel = orderPagedResourcesAssembler.toModel(orders, orderModelAssembler);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}
