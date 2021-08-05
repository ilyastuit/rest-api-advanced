package com.epam.esm.api.assembler;

import com.epam.esm.api.v1.UserController;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderModel;
import com.epam.esm.entity.user.User;
import com.epam.esm.entity.user.UserModel;
import lombok.SneakyThrows;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    private final GiftCertificateModelAssembler giftCertificateModelAssembler;

    public UserModelAssembler(GiftCertificateModelAssembler giftCertificateModelAssembler) {
        super(UserController.class, UserModel.class);
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
    }

    @SneakyThrows
    @Override
    public UserModel toModel(User entity) {
        UserModel model = instantiateModel(entity);

        model.setId(entity.getId());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setCreateDate(entity.getCreateDate());
        model.setLastUpdateDate(entity.getLastUpdateDate());
        model.setOrders(getOrderModelsFromOrders(entity.getOrders(), model));

        model.add(
                linkTo(
                        methodOn(UserController.class)
                                .one(entity.getId())
                ).withSelfRel()
        );

        return model;
    }


    private List<OrderModel> getOrderModelsFromOrders(List<Order> orders, UserModel userModel) {
        if (orders.isEmpty())
            return Collections.emptyList();

        return orders.stream()
                .map(order -> OrderModel.builder()
                        .id(order.getId())
                        .price(order.getPrice())
                        .status(order.getStatus())
                        .orderDate(order.getOrderDate())
                        .lastUpdateDate(order.getLastUpdateDate())
                        .user(userModel)
                        .giftCertificate(giftCertificateModelAssembler.toModel(order.getGiftCertificate()))
                        .build())
                .collect(Collectors.toList());
    }
}
