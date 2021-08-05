package com.epam.esm.cli.infrastructure.param.resolver;

import com.epam.esm.cli.infrastructure.param.CommandData;
import com.epam.esm.cli.infrastructure.param.annotations.ParamMapper;
import com.epam.esm.cli.infrastructure.param.handler.Handler;
import com.epam.esm.cli.infrastructure.param.parser.Parser;
import com.epam.esm.cli.infrastructure.param.exception.InvalidParamException;
import com.epam.esm.cli.infrastructure.param.exception.HandlerNotFoundException;
import org.reflections.Reflections;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParamResolver implements Resolver {

    private final Parser paramParser;
    private final JdbcTemplate jdbcTemplate;

    public ParamResolver(Parser paramParser, JdbcTemplate jdbcTemplate) {
        this.paramParser = paramParser;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Handler resolve(String param) throws HandlerNotFoundException, InvalidParamException {
        CommandData data = paramParser.parse(param);

        return resolveHandler(data);
    }

    private Handler resolveHandler(CommandData data) throws HandlerNotFoundException {
        Set<Class<? extends Handler>> handlers = getHandlers();

        for (Class<? extends Handler> handler : handlers) {
            if (handler.getAnnotation(ParamMapper.class).name().equals(data.getName())) {
                try {
                    Class<?> c = Class.forName(handler.getName());
                    return (Handler) c.getConstructor(CommandData.class, JdbcTemplate.class).newInstance(data, jdbcTemplate);
                } catch (IllegalArgumentException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }

        throw new HandlerNotFoundException(data);
    }

    public Set<Class<? extends Handler>> getHandlers() {
        Reflections reflections = new Reflections("com.epam.esm.cli.*");

        return reflections.getSubTypesOf(Handler.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(ParamMapper.class))
                .collect(Collectors.toSet());
    }
}
