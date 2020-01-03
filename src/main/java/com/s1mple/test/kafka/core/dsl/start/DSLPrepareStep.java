package com.s1mple.test.kafka.core.dsl.start;

public interface DSLPrepareStep {

    /**
     * you can set customized cluster ips in three ways:
     * <p>
     * 1. {@code suite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")}
     * 2. {@code suite.clusterIps("172.20.10.120:9092", "172.20.10.152:9092", "172.20.10.171:9092")}
     * 3. {@code suite.clusterIps("172.20.10.120:9092,172.20.10.152:9092", "172.20.10.171:9092")}
     * <p>
     * if you not set communally, load cluster ips from cloud.yml.
     */
    DSLStartStep clusterIps(String ip, String... others);

    DSLStartStep ssl(String configFilePath);

    DSLStartStep ssl(KafkaConfig config);

    /**
     * remove ssl properties "security.protocol=SSL"
     */
    DSLStartStep noSsl();
}
