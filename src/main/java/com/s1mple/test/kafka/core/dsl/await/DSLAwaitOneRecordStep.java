package com.s1mple.test.kafka.core.dsl.await;

import com.alibaba.fastjson.JSONArray;

public interface DSLAwaitOneRecordStep extends DSLAwaitRecordStep, DSLAwaitOneRecordOutputStep, DSLAwaitOneRecordConvertStep {

    @Override
    String toString();

    @Override
    JsonObject toJsonObject();

    @Override
    JSONArray toJsonArray();

    @Override
    void toXml();

    @Override
    <T> T toJavaObject(Class<T> clazz);

    @Override
    void writeAsTxt(String absolutePath);

    @Override
    void writeAsTxt(ResourceFile resourceFile);
}
