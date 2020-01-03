package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.*;

import java.util.Properties;
import java.util.Set;

public class DSLAwaitIndexStepImpl implements DSLAwaitIndexStep {
    private final Properties properties;
    private final Set<KafkaConfig.Item> items;
    private final long timeout;

    public DSLAwaitIndexStepImpl(Properties properties, Set<KafkaConfig.Item> items, long timeout) {
        this.properties = properties;
        this.items = items;
        this.timeout = timeout;
    }

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer Fetch latest one record
    // --------------------------------------------------------------------------------------------

    @Override
    public DSLAwaitOneFromStep latestOne() {
        return one(-1);
    }

    @Override
    public DSLAwaitOneFromStep one() {
        return latestOne();
    }

    @Override
    public DSLAwaitOneFromStep lastOne() {
        return latestOne();
    }

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer Fetch record by index in records
    // --------------------------------------------------------------------------------------------

    @Override
    public DSLAwaitOneFromStep one(int index) {
        return new DSLAwaitOneFromStepImpl(properties, items, timeout, index);
    }

    @Override
    public DSLAwaitOneFromStep firstOne() {
        return one(0);
    }

    @Override
    public DSLAwaitOneFromStep secondOne() {
        return one(1);
    }

    @Override
    public DSLAwaitOneFromStep thirdOne() {
        return one(2);
    }

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer Fetch all latest records
    // --------------------------------------------------------------------------------------------

    @Override
    public DSLAwaitMultiFromStep all() {
        return new DSLAwaitMultiFromStepImpl(properties, items, timeout);
    }

    @Override
    public DSLAwaitMultiFromStep multi() {
        return all();
    }

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer Fetch multi records directly from topic
    // --------------------------------------------------------------------------------------------

    @Override
    public DSLAwaitMultiRecordsStep fromTopic(String topic) {
        return all().fromTopic(topic);
    }

    @Override
    public DSLAwaitMultiRecordsStep fromTopic(String topic, String groupId) {
        return all().fromTopic(topic, groupId);
    }

    @Override
    public DSLAwaitMultiLoadConfigStep fromTopic() {
        return all().fromTopic();
    }
}
