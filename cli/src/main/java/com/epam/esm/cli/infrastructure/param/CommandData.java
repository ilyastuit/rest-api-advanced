package com.epam.esm.cli.infrastructure.param;

public class CommandData {

    private final String name;
    private final String value;

    public CommandData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
