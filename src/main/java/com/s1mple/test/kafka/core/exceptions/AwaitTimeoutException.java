package com.s1mple.test.kafka.core.exceptions;

public class AwaitTimeoutException extends RuntimeException {

    public AwaitTimeoutException(String message) {
        super(message);
    }

    public AwaitTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
