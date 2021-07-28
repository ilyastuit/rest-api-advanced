package com.epam.esm.cli.infrastructure.param.parser;

import com.epam.esm.cli.infrastructure.param.CommandData;
import com.epam.esm.cli.infrastructure.param.exception.InvalidCommandException;
import org.springframework.stereotype.Service;

@Service
public class ParamParser implements Parser {

    @Override
    public CommandData parse(String param) throws InvalidCommandException {
        String[] seperatedParam = param.split("=");

        if (seperatedParam.length < 2) {
            throw new InvalidCommandException(param);
        }

        return new CommandData(seperatedParam[0], seperatedParam[1]);
    }

}
