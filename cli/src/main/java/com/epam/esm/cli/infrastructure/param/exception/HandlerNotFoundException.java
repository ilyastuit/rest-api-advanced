package com.epam.esm.cli.infrastructure.param.exception;

import com.epam.esm.cli.infrastructure.param.CommandData;

public class HandlerNotFoundException extends Exception {

    public HandlerNotFoundException(CommandData command) {
        super("Handler not found for command: " + command.getName() + " with value: " + command.getValue());
    }
}
