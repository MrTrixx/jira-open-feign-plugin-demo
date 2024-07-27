package ru.jira.open.feign.plugin.demo.exception;

public final class ClassNotFoundExceptionWrapper extends RuntimeException {
    public ClassNotFoundExceptionWrapper(final Throwable cause) {
        super(cause);
    }
}
