package com.s1mple.test.kafka.core.dsl.await;

import java.util.List;

public interface DSLAwaitMultiRecordsStep extends DSLAwaitRecordStep, DSLAwaitMultiRecordsFetchOneStep, DSLAwaitMultiRecordsConvertStep {

    int size();

    @Override
    List<String> toList();

    @Override
    <T> List<T> toJavaObjects(Class<T> clazz);

    @Override
    DSLAwaitOneRecordStep fetchLatestOne();

    @Override
    DSLAwaitOneRecordStep fetchSecondLastOne();

    @Override
    DSLAwaitOneRecordStep fetchThirdLastOne();

    @Override
    DSLAwaitOneRecordStep fetchLastOne();

    @Override
    DSLAwaitOneRecordStep fetchOne(int index);

    @Override
    DSLAwaitOneRecordStep fetchFirstOne();

    @Override
    DSLAwaitOneRecordStep fetchSecondOne();

    @Override
    DSLAwaitOneRecordStep fetchThirdOne();
}
