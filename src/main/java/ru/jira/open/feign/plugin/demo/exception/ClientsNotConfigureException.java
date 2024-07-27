package ru.jira.open.feign.plugin.demo.exception;

public class ClientsNotConfigureException extends RuntimeException {

    public ClientsNotConfigureException(final Throwable cause) {
        super(cause);
    }
}
