package com.epam.esm.service.exceptions;

public class TagNotFoundException extends Exception {
    public TagNotFoundException(int id) {
        super("Tag is not found (id = " + id + ")");
    }

    public TagNotFoundException(String name) {
        super("Tag is not found (name = " + name + ")");
    }
}
