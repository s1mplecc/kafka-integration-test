package com.s1mple.test.kafka.core.dsl.await;

public interface DSLAwaitOneRecordOutputStep {

    /**
     * @param absolutePath absolute file path
     */
    void writeAsTxt(String absolutePath);

    /**
     * @param resourceFile file path based on resources
     */
    void writeAsTxt(ResourceFile resourceFile);
}
