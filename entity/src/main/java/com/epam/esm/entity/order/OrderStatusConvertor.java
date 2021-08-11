package com.epam.esm.entity.order;

import com.epam.esm.entity.exceptions.IllegalOrderStatusValueException;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConvertor implements AttributeConverter<OrderStatus, String> {


    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.getValue();
    }

    @SneakyThrows
    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        OrderStatus result = null;
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getValue().equals(dbData)) {
                result = OrderStatus.fromValue(dbData);
            }
        }
        if (result == null) {
            throw new IllegalOrderStatusValueException(dbData);
        }
        return result;
    }
}
