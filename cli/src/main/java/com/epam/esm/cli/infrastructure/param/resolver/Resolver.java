package com.epam.esm.cli.infrastructure.param.resolver;

import com.epam.esm.cli.infrastructure.param.handler.Handler;
import com.epam.esm.cli.infrastructure.param.exception.HandlerNotFoundException;
import com.epam.esm.cli.infrastructure.param.exception.InvalidCommandException;

public interface Resolver {

    public Handler resolve(String path) throws HandlerNotFoundException, InvalidCommandException;

}
