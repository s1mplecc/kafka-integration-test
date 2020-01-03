package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitMultiRecordsStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitOneRecordStep;

import java.util.List;
import java.util.stream.Collectors;

public class DSLAwaitMultiRecordsStepImpl implements DSLAwaitMultiRecordsStep {
    private final List<String> records;

    public DSLAwaitMultiRecordsStepImpl(List<String> records) {
        this.records = records;
    }

    @Override
    public int size() {
        return records.size();
    }

    @Override
    public List<String> toList() {
        return records;
    }

    @Override
    public <T> List<T> toJavaObjects(Class<T> clazz) {
        return records.parallelStream()
                .map(record -> new DSLAwaitOneRecordStepImpl(record).toJavaObject(clazz))
                .collect(Collectors.toList());
    }

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer Fetch latest one record
    // --------------------------------------------------------------------------------------------

    @Override
    public DSLAwaitOneRecordStep fetchLatestOne() {
        return fetchOne(-1);
    }

    @Override
    public DSLAwaitOneRecordStep fetchSecondLastOne() {
        return fetchOne(records.size() - 2);
    }

    @Override
    public DSLAwaitOneRecordStep fetchThirdLastOne() {
        return fetchOne(records.size() - 3);
    }

    @Override
    public DSLAwaitOneRecordStep fetchLastOne() {
        return fetchLatestOne();
    }

    // --------------------------------------------------------------------------------------------
    //  Kafka Consumer Fetch record by index in records
    // --------------------------------------------------------------------------------------------

    @Override
    public DSLAwaitOneRecordStep fetchOne(int index) {
        return new DSLAwaitOneRecordStepImpl(records, index);
    }

    @Override
    public DSLAwaitOneRecordStep fetchFirstOne() {
        return fetchOne(0);
    }

    @Override
    public DSLAwaitOneRecordStep fetchSecondOne() {
        return fetchOne(1);
    }

    @Override
    public DSLAwaitOneRecordStep fetchThirdOne() {
        return fetchOne(2);
    }
}
