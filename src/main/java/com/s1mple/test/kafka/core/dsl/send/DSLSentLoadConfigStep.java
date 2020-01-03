package com.s1mple.test.kafka.core.dsl.send;

public interface DSLSentLoadConfigStep {

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
    void ofConfig(String label);

    /**
     * @param configFile config file load
     * @param label      kafka items label (or system) in configFile
     */
    void ofConfig(String configFile, String label);
}
