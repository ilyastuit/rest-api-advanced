package com.epam.esm.cli.infrastructure.param.resolver;

import com.epam.esm.cli.infrastructure.param.CommandData;
import com.epam.esm.cli.infrastructure.param.annotations.ParamMapper;
import com.epam.esm.cli.infrastructure.param.handler.Handler;
import com.epam.esm.cli.infrastructure.param.parser.Parser;
import com.epam.esm.cli.infrastructure.param.exception.InvalidCommandException;
import com.epam.esm.cli.infrastructure.param.exception.HandlerNotFoundException;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
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
    public Handler resolve(String param) throws HandlerNotFoundException, InvalidCommandException {
        CommandData data = paramParser.parse(param);

        return resolveHandler(data);
    }

    private Handler resolveHandler(CommandData data) throws HandlerNotFoundException {
        Set<Class<Handler>> handlers = getHandlers();

        for (Class<?> handler : handlers) {
            if (handler.getAnnotation(ParamMapper.class).name().equals(data.getName())) {
                try {
                    Class<Handler> c = (Class<Handler>) Class.forName(handler.getName());
                    return c.getConstructor(CommandData.class, JdbcTemplate.class).newInstance(data, jdbcTemplate);
                } catch (IllegalArgumentException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }

        throw new HandlerNotFoundException(data);
    }

    public Set<Class<Handler>> getHandlers() {
        Reflections reflections = new Reflections("com.epam.esm.cli.*");

        return reflections.getSubTypesOf(Handler.class)
                .stream()
                .map(clazz -> (Class<Handler>) clazz)
                .filter(clazz -> clazz.isAnnotationPresent(ParamMapper.class))
                .collect(Collectors.toSet());
    }
}
