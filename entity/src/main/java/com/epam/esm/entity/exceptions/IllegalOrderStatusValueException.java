package com.epam.esm.entity.exceptions;

public class IllegalOrderStatusValueException extends Exception {

    public IllegalOrderStatusValueException(String value) {
        super("Order status value is not found in enum list <value = "+ value +">.");
    }
}
