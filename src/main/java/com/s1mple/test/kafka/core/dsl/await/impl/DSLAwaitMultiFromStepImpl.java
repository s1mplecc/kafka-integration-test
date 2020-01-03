package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitMultiFromStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitMultiLoadConfigStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitMultiRecordsStep;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DSLAwaitMultiFromStepImpl extends AbstractDSLAwaitFromStep implements DSLAwaitMultiFromStep {

    DSLAwaitMultiFromStepImpl(Properties properties, Set<KafkaConfig.Item> items, long timeout) {
        super(properties, items, timeout);
    }

    @Override
    public final DSLAwaitMultiLoadConfigStep fromTopic() {
        return new DSLAwaitMultiLoadConfigStepImpl(this, items);
    }

    @Override
    public final DSLAwaitMultiRecordsStep fromTopic(String topic) {
        return (DSLAwaitMultiRecordsStep) super.fromTopic(topic);
    }

    @Override
    public final DSLAwaitMultiRecordsStep fromTopic(String topic, String groupId) {
        return (DSLAwaitMultiRecordsStep) super.fromTopic(topic, groupId);
    }

    @Override
    protected final DSLAwaitMultiRecordsStep awaitRecord(FutureTask<List<String>> consumerTask) throws InterruptedException, ExecutionException, TimeoutException {
        return new DSLAwaitMultiRecordsStepImpl(consumerTask.get(timeout, TimeUnit.SECONDS));
    }
}
