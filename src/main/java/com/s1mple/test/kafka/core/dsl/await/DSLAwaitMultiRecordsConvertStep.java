package com.s1mple.test.kafka.core.dsl.await;

import java.util.List;

public interface DSLAwaitMultiRecordsConvertStep {
    List<String> toList();

    <T> List<T> toJavaObjects(Class<T> clazz);
}
