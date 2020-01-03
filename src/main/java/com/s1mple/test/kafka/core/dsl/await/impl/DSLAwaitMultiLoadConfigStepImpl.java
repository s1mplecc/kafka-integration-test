package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitFromStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitMultiLoadConfigStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitMultiRecordsStep;

import java.util.Set;

public class DSLAwaitMultiLoadConfigStepImpl extends AbstractDSLAwaitLoadConfigStep implements DSLAwaitMultiLoadConfigStep {
    public DSLAwaitMultiLoadConfigStepImpl(DSLAwaitFromStep dslAwaitFromStep, Set<KafkaConfig.Item> items) {
        super(dslAwaitFromStep, items);
    }

    @Override
    public final DSLAwaitMultiRecordsStep ofConfig(String label) {
        return (DSLAwaitMultiRecordsStep) super.ofConfig(label);
    }

    @Override
    public final DSLAwaitMultiRecordsStep ofConfig(String configFile, String label) {
        return (DSLAwaitMultiRecordsStep) super.ofConfig(configFile, label);
    }
}
