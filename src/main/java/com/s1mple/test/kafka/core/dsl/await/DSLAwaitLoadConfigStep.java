package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitLoadConfigStep {
    DSLAwaitRecordStep ofConfig(String label);

    DSLAwaitRecordStep ofConfig(String configFile, String label);
}
