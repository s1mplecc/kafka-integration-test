package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitIndexStep extends DSLAwaitMultiFromStep {

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer fetch latest one record
    // --------------------------------------------------------------------------------------------

    DSLAwaitOneFromStep latestOne();

    DSLAwaitOneFromStep one();

    DSLAwaitOneFromStep lastOne();

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer fetch record by index in records
    // --------------------------------------------------------------------------------------------

    DSLAwaitOneFromStep one(int index);

    DSLAwaitOneFromStep firstOne();

    DSLAwaitOneFromStep secondOne();

    DSLAwaitOneFromStep thirdOne();

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer fetch multi records
    // --------------------------------------------------------------------------------------------

    DSLAwaitMultiFromStep all();

    DSLAwaitMultiFromStep multi();

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer fetch multi records directly followed by fromTopic
    // --------------------------------------------------------------------------------------------

    @Override
    DSLAwaitMultiLoadConfigStep fromTopic();

    @Override
    DSLAwaitMultiRecordsStep fromTopic(String topic);

    @Override
    DSLAwaitMultiRecordsStep fromTopic(String topic, String groupId);
}
