package com.s1mple.test.kafka;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitIndexStep;
import com.s1mple.test.kafka.core.dsl.await.impl.DSLAwaitIndexStepImpl;
import com.s1mple.test.kafka.core.dsl.send.DSLSendToStep;
import com.s1mple.test.kafka.core.dsl.send.impl.DSLSendToStepImpl;
import com.s1mple.test.kafka.core.dsl.start.DSLStartStep;
import com.s1mple.test.kafka.core.exceptions.SendNullValueException;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.admin.AdminClientConfig.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.*;

public class KafkaSuite implements DSLStartStep {
    private static final Logger log = LoggerFactory.getLogger(KafkaSuite.class);

    private final Properties properties;
    private final Set<Item> items;

    public KafkaSuite() {
        this(null);
    }

    public KafkaSuite(KafkaConfig config) {
        properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        items = new HashSet<>();
        if (config != null) {
            log.info("KafkaSuite load kafka config:\n{}", TableFormatter.formatObject(config, "log", "items"));
            properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, config.clusterIps());
            ssl(config);

            items.addAll(config.items());
        }
    }

    void fillWith(Set<Item> kafkaItems) {
        items.addAll(kafkaItems);
        log.info("KafkaSuite filling in kafka items loaded from flows customized config:\n{}", TableFormatter.formatCollections(items));
    }

    // --------------------------------------------------------------------------------------------
    //  Fluent API for integration test
    // --------------------------------------------------------------------------------------------

    /**
     * you can set customized kafka cluster ips in three ways:
     * <p>
     * 1. {@code kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")}
     * 2. {@code kafkaSuite.clusterIps("172.20.10.120:9092", "172.20.10.152:9092", "172.20.10.171:9092")}
     * 3. {@code kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092", "172.20.10.171:9092")}
     * <p>
     * if you not set communally, load kafka cluster ips from cloud.yml.
     */
    @Override
    public DSLStartStep clusterIps(String ip, String... others) {
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG,
                others.length == 0 ?
                        ip :
                        Arrays.stream(others).reduce(ip, (x, y) -> x + "," + y));
        return this;
    }

    @Override
    public DSLStartStep ssl(String configFilePath) {
        return ssl(ConfigFactory.load(configFilePath).kafka());
    }

    @Override
    public DSLStartStep ssl(KafkaConfig config) {
        if (SecurityProtocol.SSL.name.equalsIgnoreCase(config.sslProtocol())) {
            properties.setProperty(SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name);
            properties.setProperty(SSL_TRUSTSTORE_LOCATION_CONFIG, config.sslTruststoreLocation());
            properties.setProperty(SSL_TRUSTSTORE_PASSWORD_CONFIG, config.sslTruststorePassword());
            properties.setProperty(SSL_KEYSTORE_LOCATION_CONFIG, config.sslKeystoreLocation());
            properties.setProperty(SSL_KEYSTORE_PASSWORD_CONFIG, config.sslKeystorePassword());
            properties.setProperty(SSL_KEY_PASSWORD_CONFIG, config.sslKeyPassword());
        }
        return this;
    }

    @Override
    public DSLStartStep noSsl() {
        properties.remove(SECURITY_PROTOCOL_CONFIG);
        return this;
    }


    @Override
    public DSLSendToStep send(String value) {
        if (value == null) {
            throw new SendNullValueException("can not send null value to kafka.");
        }
        return new DSLSendToStepImpl(properties, items, value);
    }

    @Override
    public DSLSendToStep send(ResourceFile file) throws ResourceFileNotFoundException {
        return send(file.content());
    }

    @Override
    public DSLSendToStep send(InputStream inputStream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        return send(writer.toString());
    }

    /**
     * by default, waiting 60s for the result listening in kafka topic
     */
    @Override
    public DSLAwaitIndexStep await() {
        return await(60);
    }

    /**
     * @param seconds The time, in seconds,
     *                waiting for the result listening in kafka topic
     */
    @Override
    public DSLAwaitIndexStep await(long seconds) {
        return new DSLAwaitIndexStepImpl(properties, items, seconds);
    }
}
