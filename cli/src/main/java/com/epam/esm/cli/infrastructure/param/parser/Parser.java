package com.epam.esm.cli.infrastructure.param.parser;

import com.epam.esm.cli.infrastructure.param.CommandData;
import com.epam.esm.cli.infrastructure.param.exception.InvalidParamException;

public interface Parser {

    public CommandData parse(String param) throws InvalidParamException;

}
