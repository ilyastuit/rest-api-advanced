package com.epam.esm.entity.order;

import com.epam.esm.entity.exceptions.IllegalOrderStatusValueException;

public enum OrderStatus {
    PAID("paid"), UNPAID("unpaid"), CANCELED("canceled"), DONE("done");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatus fromValue(String value) throws IllegalOrderStatusValueException {
        for (OrderStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }

        throw new IllegalOrderStatusValueException(value);
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "value='" + value + '\'' +
                '}';
    }
}
