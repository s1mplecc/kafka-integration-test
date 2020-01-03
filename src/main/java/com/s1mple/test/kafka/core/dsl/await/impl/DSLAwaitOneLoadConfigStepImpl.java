package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitFromStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitOneLoadConfigStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitOneRecordStep;

import java.util.Set;

public class DSLAwaitOneLoadConfigStepImpl extends AbstractDSLAwaitLoadConfigStep implements DSLAwaitOneLoadConfigStep {
    public DSLAwaitOneLoadConfigStepImpl(DSLAwaitFromStep dslAwaitFromStep, Set<KafkaConfig.Item> items) {
        super(dslAwaitFromStep, items);
    }

    @Override
    public final DSLAwaitOneRecordStep ofConfig(String label) {
        return (DSLAwaitOneRecordStep) super.ofConfig(label);
    }

    @Override
    public final DSLAwaitOneRecordStep ofConfig(String configFile, String label) {
        return (DSLAwaitOneRecordStep) super.ofConfig(configFile, label);
    }
}
