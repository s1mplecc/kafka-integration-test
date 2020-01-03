package com.s1mple.test.kafka.core.dsl.send;

import java.io.IOException;
import java.io.InputStream;

public interface DSLSendStep {
    DSLSendToStep send(String value);

    DSLSendToStep send(ResourceFile file) throws ResourceFileNotFoundException;

    DSLSendToStep send(InputStream inputStream) throws IOException;
}
