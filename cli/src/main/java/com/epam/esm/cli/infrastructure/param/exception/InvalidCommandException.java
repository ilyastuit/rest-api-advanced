package com.epam.esm.cli.infrastructure.param.exception;

public class InvalidCommandException extends Exception{

    public InvalidCommandException(String command) {
        super("Command does not meet the requirements: " + command);
    }
}
