package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitFromStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitLoadConfigStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitRecordStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class AbstractDSLAwaitLoadConfigStep implements DSLAwaitLoadConfigStep {
    private static final Logger log = LoggerFactory.getLogger(AbstractDSLAwaitLoadConfigStep.class);
    private static final String GROUP_ID_POSTFIX = "_INTEGRATION_TEST";

    private final Set<KafkaConfig.Item> items;
    private final DSLAwaitFromStep dslAwaitFromStep;

    public AbstractDSLAwaitLoadConfigStep(DSLAwaitFromStep dslAwaitFromStep, Set<KafkaConfig.Item> items) {
        this.dslAwaitFromStep = dslAwaitFromStep;
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
    public DSLAwaitRecordStep ofConfig(String label) {
        KafkaConfig.Item item = KafkaConfig.pickOf(items, label);
        log.info("Kafka await step load TOPIC {} and GROUP_ID {} with LABEL {} from config."
                , item.topic(), item.group(), label);
        return dslAwaitFromStep.fromTopic(item.topic(), item.group() + GROUP_ID_POSTFIX);
    }

    /**
     * @param configFile config file load
     * @param label      kafka items label (or system) in configFile
     */
    @Override
    public DSLAwaitRecordStep ofConfig(String configFile, String label) {
        KafkaConfig.Item item = KafkaConfig.pickOf(ConfigFactory.load(configFile).kafka().items(), label);
        log.info("Kafka await step load TOPIC {} and GROUP_ID {} with LABEL {} from config file."
                , item.topic(), item.group(), label, configFile);
        return dslAwaitFromStep.fromTopic(item.topic(), item.group());
    }
}
