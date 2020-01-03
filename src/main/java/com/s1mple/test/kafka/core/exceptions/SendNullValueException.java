package com.s1mple.test.kafka.core.exceptions;

public class SendNullValueException extends RuntimeException {

    public SendNullValueException(String message) {
        super(message);
    }

    public SendNullValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
