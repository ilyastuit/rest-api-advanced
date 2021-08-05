package com.epam.esm.cli.infrastructure.param.resolver;

import com.epam.esm.cli.infrastructure.param.handler.Handler;
import com.epam.esm.cli.infrastructure.param.exception.HandlerNotFoundException;
import com.epam.esm.cli.infrastructure.param.exception.InvalidParamException;

public interface Resolver {

    public Handler resolve(String path) throws HandlerNotFoundException, InvalidParamException;

}
