package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitOneRecordConvertStep {

    @Override
    String toString();

    JsonObject toJsonObject();

    JSONArray toJsonArray();

    void toXml();

    <T> T toJavaObject(Class<T> clazz);
}
