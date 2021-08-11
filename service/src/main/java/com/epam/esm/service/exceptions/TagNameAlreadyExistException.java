package com.epam.esm.service.exceptions;

public class TagNameAlreadyExistException extends Exception {

    public TagNameAlreadyExistException(String name) {
        super("Tag name <" + name + "> is already exist.");
    }
}
