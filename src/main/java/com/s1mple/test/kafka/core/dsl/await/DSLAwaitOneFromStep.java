package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitOneFromStep extends DSLAwaitFromStep {

    /**
     * {@link #fromTopic(String topic, String groupId)}, default groupId is integration-test.
     */
    @Override
    DSLAwaitOneRecordStep fromTopic(String topic);

    @Override
    DSLAwaitOneRecordStep fromTopic(String topic, String groupId);

    /**
     * read topic and groupId from config file
     */
    @Override
    DSLAwaitOneLoadConfigStep fromTopic();
}
