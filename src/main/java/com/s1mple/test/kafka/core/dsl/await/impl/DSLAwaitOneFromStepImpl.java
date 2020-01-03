package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitOneFromStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitOneLoadConfigStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitOneRecordStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitRecordStep;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DSLAwaitOneFromStepImpl extends AbstractDSLAwaitFromStep implements DSLAwaitOneFromStep {
    private final int index;

    public DSLAwaitOneFromStepImpl(Properties properties, Set<KafkaConfig.Item> items, long timeout, int index) {
        super(properties, items, timeout);
        this.index = index;
    }

    @Override
    public final DSLAwaitOneLoadConfigStep fromTopic() {
        return new DSLAwaitOneLoadConfigStepImpl(this, items);
    }

    @Override
    public final DSLAwaitOneRecordStep fromTopic(String topic) {
        return (DSLAwaitOneRecordStep) super.fromTopic(topic);
    }

    @Override
    public final DSLAwaitOneRecordStep fromTopic(String topic, String groupId) {
        return (DSLAwaitOneRecordStep) super.fromTopic(topic, groupId);
    }

    @Override
    protected final DSLAwaitRecordStep awaitRecord(FutureTask<List<String>> consumerTask) throws InterruptedException, ExecutionException, TimeoutException {
        return new DSLAwaitOneRecordStepImpl(consumerTask.get(timeout, TimeUnit.SECONDS), index);
    }
}
