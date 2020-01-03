package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitMultiFromStep extends DSLAwaitFromStep {

    /**
     * {@link #fromTopic(String topic, String groupId)}, default groupId is integration-test.
     */
    @Override
    DSLAwaitMultiRecordsStep fromTopic(String topic);

    @Override
    DSLAwaitMultiRecordsStep fromTopic(String topic, String groupId);

    /**
     * read topic and groupId from config file
     */
    @Override
    DSLAwaitMultiLoadConfigStep fromTopic();
}
