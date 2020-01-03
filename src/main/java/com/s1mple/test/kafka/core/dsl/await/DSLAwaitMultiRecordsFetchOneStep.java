package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitMultiRecordsFetchOneStep {
    DSLAwaitOneRecordStep fetchLatestOne();

    DSLAwaitOneRecordStep fetchLastOne();

    DSLAwaitOneRecordStep fetchSecondLastOne();

    DSLAwaitOneRecordStep fetchThirdLastOne();

    DSLAwaitOneRecordStep fetchOne(int index);

    DSLAwaitOneRecordStep fetchFirstOne();

    DSLAwaitOneRecordStep fetchSecondOne();

    DSLAwaitOneRecordStep fetchThirdOne();
}
