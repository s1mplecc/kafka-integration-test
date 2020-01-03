package com.s1mple.test.kafka.core.dsl.start;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitIndexStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitStep;
import com.s1mple.test.kafka.core.dsl.send.DSLSendStep;
import com.s1mple.test.kafka.core.dsl.send.DSLSendToStep;

import java.io.IOException;
import java.io.InputStream;

public interface DSLStartStep extends DSLSendStep, DSLAwaitStep, DSLPrepareStep {
    @Override
    DSLAwaitIndexStep await();

    @Override
    DSLAwaitIndexStep await(long seconds);

    @Override
    DSLSendToStep send(String value);

    @Override
    DSLSendToStep send(ResourceFile file) throws ConfigFileNotFoundException;

    @Override
    DSLSendToStep send(InputStream inputStream) throws IOException;

    @Override
    DSLStartStep clusterIps(String ip, String... others);

    @Override
    DSLStartStep ssl(String configFilePath);

    @Override
    DSLStartStep ssl(KafkaConfig config);

    @Override
    DSLStartStep noSsl();
}
