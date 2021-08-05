package com.epam.esm.api.assembler;

import com.epam.esm.api.v1.OrderController;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.order.OrderModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class OrderModelAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel> {

    private final UserModelAssembler userModelAssembler;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;

    public OrderModelAssembler(UserModelAssembler userModelAssembler, GiftCertificateModelAssembler giftCertificateModelAssembler) {
        super(OrderController.class, OrderModel.class);
        this.userModelAssembler = userModelAssembler;
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
    }

    @Override
    public OrderModel toModel(Order entity) {
        OrderModel model = instantiateModel(entity);

        model.setId(entity.getId());
        model.setPrice(entity.getPrice());
        model.setStatus(entity.getStatus());
        model.setUser(userModelAssembler.toModel(entity.getUser()));
        model.setGiftCertificate(giftCertificateModelAssembler.toModel(entity.getGiftCertificate()));
        model.setOrderDate(entity.getOrderDate());
        model.setLastUpdateDate(entity.getLastUpdateDate());

        return model;
    }
}
