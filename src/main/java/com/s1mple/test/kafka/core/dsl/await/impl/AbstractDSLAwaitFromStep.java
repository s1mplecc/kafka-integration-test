package com.s1mple.test.kafka.core.dsl.await.impl;

import com.s1mple.test.kafka.core.dsl.await.DSLAwaitFromStep;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitRecordStep;
import com.s1mple.test.kafka.core.exceptions.AwaitTimeoutException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public abstract class AbstractDSLAwaitFromStep implements DSLAwaitFromStep {
    private static final Logger log = LoggerFactory.getLogger(AbstractDSLAwaitFromStep.class);

    protected final Properties properties;
    protected final Set<KafkaConfig.Item> items;
    protected final long timeout;

    private FutureTask<List<String>> consumerTask;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public AbstractDSLAwaitFromStep(Properties properties, Set<KafkaConfig.Item> items, long timeout) {
        this.properties = new Properties();
        this.items = items;
        this.timeout = timeout;

        this.properties.putAll(properties);
        this.properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // turn off auto commit, using consumer.commitSync() make sure data consistency
        this.properties.setProperty(ENABLE_AUTO_COMMIT_CONFIG, "false");
        this.properties.setProperty(AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        this.properties.setProperty(AUTO_OFFSET_RESET_CONFIG, "latest");
        this.properties.setProperty(SESSION_TIMEOUT_MS_CONFIG, "10000");
        // heartbeat interval send time to make sure consumer is alive or dead
        this.properties.setProperty(HEARTBEAT_INTERVAL_MS_CONFIG, "2000");
    }

    /**
     * {@link #fromTopic(String topic, String groupId)}, default groupId is integration-test.
     */
    @Override
    public DSLAwaitRecordStep fromTopic(String topic) {
        return fromTopic(topic, "integration-test");
    }

    @Override
    public DSLAwaitRecordStep fromTopic(String topic, String groupId) {
        properties.setProperty(GROUP_ID_CONFIG, groupId);

        try {
            consumerTask = createConsumerTask(topic);
            executor.execute(consumerTask);

            return awaitRecord(consumerTask);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            log.warn("Awaiting for listening in kafka throw exception.", ex);
            throw new AwaitTimeoutException(
                    String.format("can not fetch anything from kafka topic \"%s\" with groupId \"%s\" of cluster(%s) in %ss.",
                            topic, groupId, properties.getProperty(BOOTSTRAP_SERVERS_CONFIG), timeout), ex);
        } finally {
            try {
                log.info("Closing kafka consumer listened in TOPIC {} and GROUP_ID {}.", topic, groupId);
                consumerTask.cancel(true);
                executor.shutdown();
            } catch (ConcurrentModificationException ex) {
                log.warn(ex.getMessage());
            }
        }
    }

    private FutureTask<List<String>> createConsumerTask(String topic) {
        return new FutureTask<>(() -> {
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singletonList(topic));
            String groupId = properties.getProperty(GROUP_ID_CONFIG);
            log.info("Kafka consumer subscribe TOPIC {} with GROUP_ID {}", topic, groupId);

            while (true) {
                ConsumerRecords<String, String> cRecords = consumer.poll(100);
                if (cRecords != null && !cRecords.isEmpty()) {
                    List<String> records = new ArrayList<>();
                    for (ConsumerRecord<String, String> record : cRecords) {
                        log.info("Fetch record from kafka TOPIC {} successfully. GROUP_ID: {}, OFFSET: {}, PARTITION: {}. Record value: {}"
                                , topic, groupId, record.offset(), record.partition(), record.value());
                        records.add(record.value());
                        consumer.commitSync();
                    }
                    consumer.unsubscribe();
                    consumer.close();
                    return records;
                }
            }
        });
    }

    protected abstract DSLAwaitRecordStep awaitRecord(FutureTask<List<String>> consumerTask) throws InterruptedException, ExecutionException, TimeoutException;
}
