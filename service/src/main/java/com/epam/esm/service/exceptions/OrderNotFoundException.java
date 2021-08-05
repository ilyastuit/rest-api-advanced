package com.epam.esm.service.exceptions;

public class OrderNotFoundException extends Exception{

    public OrderNotFoundException() {
        super("Order not found");
    }
}
