package com.s1mple.test.kafka.core.dsl.await.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitOneRecordStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DSLAwaitOneRecordStepImpl implements DSLAwaitOneRecordStep {
    private static final Logger log = LoggerFactory.getLogger(DSLAwaitOneRecordStepImpl.class);
    private final String record;

    public DSLAwaitOneRecordStepImpl(String record) {
        this.record = record;
    }

    public DSLAwaitOneRecordStepImpl(List<String> records, int index) {
        int maxIndex = records.size() - 1;
        if (index >= maxIndex || index < 0) {
            record = records.get(maxIndex);
        } else {
            record = records.get(index);
        }
    }

    @Override
    public String toString() {
        return record;
    }

    @Override
    public JsonObject toJsonObject() {
        return Optional.of(record).map(Json::parse).orElse(new JsonObject());
    }

    @Override
    public JSONArray toJsonArray() {
        return Optional.of(record).map(JSON::parseArray).orElse(new JSONArray());
    }

    @Override
    public void toXml() {
        // todo-zhangzhen
    }

    @Override
    public <T> T toJavaObject(Class<T> clazz) {
        if (record.startsWith("{") && record.endsWith("}")) {
            return Json.toJavaObject(toJsonObject(), clazz);
        } else if (record.startsWith("<") && record.endsWith(">")) {
            // todo-zz: xml parser to Java Object
        }

        return null;
    }

    /**
     * @param absolutePath absolute file path
     */
    @Override
    public void writeAsTxt(String absolutePath) {
        try (FileWriter writer = new FileWriter(new File(absolutePath))) {
            writer.write(record);
        } catch (IOException e) {
            log.error("write to file {} throw exception.", absolutePath, e);
        }
    }

    /**
     * @param resourceFile file path based on resources
     */
    @Override
    public void writeAsTxt(ResourceFile resourceFile) {
        writeAsTxt(new ResourceFile(resourceFile.name(), true).absolutePath());
    }
}
