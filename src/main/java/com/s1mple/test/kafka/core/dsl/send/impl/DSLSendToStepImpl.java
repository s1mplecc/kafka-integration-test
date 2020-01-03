package com.s1mple.test.kafka.core.dsl.send.impl;

import com.s1mple.test.kafka.core.dsl.send.DSLSendToStep;
import com.s1mple.test.kafka.core.dsl.send.DSLSentLoadConfigStep;
import com.s1mple.test.kafka.core.exceptions.SendTimeoutException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class DSLSendToStepImpl implements DSLSendToStep {
    private static final Logger log = LoggerFactory.getLogger(DSLSendToStepImpl.class);

    private final Properties properties;
    private final Set<KafkaConfig.Item> items;
    private final String value;

    public DSLSendToStepImpl(Properties properties, Set<KafkaConfig.Item> items, String value) {
        this.properties = new Properties();
        this.properties.putAll(properties);

        this.properties.setProperty(RETRIES_CONFIG, "10");
        this.properties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.properties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.properties.setProperty(MAX_REQUEST_SIZE_CONFIG, "100000000");
        this.properties.setProperty(MAX_BLOCK_MS_CONFIG, "10000");
        this.properties.setProperty(TRANSACTION_TIMEOUT_CONFIG, "3600000");

        this.items = items;
        this.value = value;
    }

    /**
     * you can using {@code toTopic("test")} or {@code toTopic(byConfig("INBOUND_IMF"))}
     */
    @Override
    public void toTopic(String topic) {
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        producer.send(new ProducerRecord<>(topic, value), (RecordMetadata metadata, Exception ex) -> {
            if (ex != null) {
                throw new SendTimeoutException(
                        String.format("Send message timeout, cluster(%s), topic \"%s\". Check your cluster ips and ssl certification in config file."
                                , properties.getProperty(BOOTSTRAP_SERVERS_CONFIG), topic), ex);
            }
            if (metadata != null) {
                log.info("Send message to kafka TOPIC {} of cluster({}) successfully.\nTOPIC: {}\nPARTITION: {}\nOFFSET: {}\nSEND MESSAGE: {}",
                        topic, properties.getProperty(BOOTSTRAP_SERVERS_CONFIG), metadata.topic(), metadata.partition(), metadata.offset(), value);
            }
        });
        producer.flush();
        producer.close();
    }

    @Override
    public DSLSentLoadConfigStep toTopic() {
        return new DSLSentLoadConfigStepImpl(this, items);
    }
}
