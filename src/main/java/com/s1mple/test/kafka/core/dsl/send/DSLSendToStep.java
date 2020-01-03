package com.s1mple.test.kafka.core.dsl.send;

public interface DSLSendToStep {
    void toTopic(String topic);

    DSLSentLoadConfigStep toTopic();
}
