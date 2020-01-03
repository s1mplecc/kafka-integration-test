package com.s1mple.test.kafka.core.exceptions;

public class InvalidIntegrationTestNameException extends RuntimeException {

    public InvalidIntegrationTestNameException(String message) {
        super(message);
    }

    public InvalidIntegrationTestNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
