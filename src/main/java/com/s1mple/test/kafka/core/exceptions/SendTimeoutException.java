package com.s1mple.test.kafka.core.exceptions;

public class SendTimeoutException extends RuntimeException {

    public SendTimeoutException(String message) {
        super(message);
    }

    public SendTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
