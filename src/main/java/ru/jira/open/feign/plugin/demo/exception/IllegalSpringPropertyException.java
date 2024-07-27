package ru.jira.open.feign.plugin.demo.exception;

public class IllegalSpringPropertyException extends RuntimeException {
    public IllegalSpringPropertyException(final String msg) {
        super(msg);
    }
}
