package com.epam.esm.cli.infrastructure.param.exception;

public class InvalidParamException extends Exception{

    public InvalidParamException(String command) {
        super("Command does not meet the requirements: " + command);
    }
}
