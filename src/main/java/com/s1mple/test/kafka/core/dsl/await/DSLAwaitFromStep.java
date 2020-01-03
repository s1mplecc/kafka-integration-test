package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitFromStep {
    /**
     * read topic and groupId load by config file
     */
    DSLAwaitLoadConfigStep fromTopic();

    DSLAwaitRecordStep fromTopic(String topic);

    DSLAwaitRecordStep fromTopic(String topic, String groupId);
}
