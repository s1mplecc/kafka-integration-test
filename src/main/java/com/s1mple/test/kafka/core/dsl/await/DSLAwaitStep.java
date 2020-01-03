package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitStep {

    DSLAwaitIndexStep await();

    DSLAwaitIndexStep await(long seconds);
}
