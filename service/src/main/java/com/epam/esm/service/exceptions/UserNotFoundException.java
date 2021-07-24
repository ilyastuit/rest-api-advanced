package com.epam.esm.service.exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(int id) {
        super("User is not found (id = "+ id +")");
    }

    public UserNotFoundException(String email) {
        super("User is not found (email = "+ email +")");
    }
}
