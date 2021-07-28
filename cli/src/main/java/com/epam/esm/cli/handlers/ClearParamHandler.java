package com.epam.esm.cli.handlers;

import com.epam.esm.cli.infrastructure.param.CommandData;
import com.epam.esm.cli.infrastructure.param.annotations.ParamMapper;
import com.epam.esm.cli.infrastructure.param.handler.Handler;
import org.springframework.jdbc.core.JdbcTemplate;

@ParamMapper(name = "clear")
public class ClearParamHandler implements Handler {

    private final CommandData data;
    private final JdbcTemplate jdbcTemplate;

    public ClearParamHandler(CommandData data, JdbcTemplate jdbcTemplate) {
        this.data = data;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void handle() {
        if (data.getValue().equals("true")) {
            jdbcTemplate.update("TRUNCATE gifts.gift_certificate, gifts.gift_certificate_tag, gifts.tag, users.list, orders.gift_certificate CASCADE;");
            System.out.println("CLEARING IS DONE!");
        }
    }

}
