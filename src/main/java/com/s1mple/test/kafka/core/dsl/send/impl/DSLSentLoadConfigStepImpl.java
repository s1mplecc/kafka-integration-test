package com.s1mple.test.kafka.core.dsl.send.impl;

import com.s1mple.test.kafka.core.dsl.send.DSLSendToStep;
import com.s1mple.test.kafka.core.dsl.send.DSLSentLoadConfigStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class DSLSentLoadConfigStepImpl implements DSLSentLoadConfigStep {
    private static final Logger log = LoggerFactory.getLogger(DSLSentLoadConfigStepImpl.class);

    private final Set<KafkaConfig.Item> items;
    private final DSLSendToStep dslSendToStep;

    public DSLSentLoadConfigStepImpl(DSLSendToStep dslSendToStep, Set<KafkaConfig.Item> items) {
        this.dslSendToStep = dslSendToStep;
        this.items = items;
    }

    /**
     * @param label label (or system) of kafka items in yaml config file.
     *              For example, pek/pek.yml define like this,
     *              <code>
     *              kafka.items:
     *              - system: flight
     *              group: flight
     *              topic: FLIGHT_ACDM_1.0
     *              </code>
     *              then you can use {@code ofConfig("flight")}
     */
    @Override
    public void ofConfig(String label) {
        KafkaConfig.Item item = KafkaConfig.pickOf(items, label);
        log.info("Kafka send step load TOPIC {} with LABEL {} from config.", item.topic(), label);
        dslSendToStep.toTopic(item.topic());
    }

    /**
     * @param configFile config file load
     * @param label      kafka items label (or system) in configFile
     */
    @Override
    public void ofConfig(String configFile, String label) {
        KafkaConfig.Item item = KafkaConfig.pickOf(ConfigFactory.load(configFile).kafka().items(), label);
        log.info("Kafka send step load TOPIC {} with LABEL {} from config file {}.", item.topic(), label, configFile);
        dslSendToStep.toTopic(item.topic());
    }
}
