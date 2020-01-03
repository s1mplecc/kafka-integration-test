package com.s1mple.test.kafka;

import com.alibaba.fastjson.JSONArray;
import com.s1mple.test.kafka.core.dsl.await.DSLAwaitMultiRecordsStep;
import com.s1mple.test.kafka.core.exceptions.AwaitTimeoutException;
import com.s1mple.test.kafka.core.exceptions.SendTimeoutException;
import lombok.Data;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaSuiteTest {
    private KafkaSuite kafkaSuite;
    private KafkaSuite sslKafkaSuite;

    @Before
    public void setUp() {
        kafkaSuite = new KafkaSuite();
        kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092");
        sslKafkaSuite = new KafkaSuite(ConfigFactory.load("cloud.yml").kafka());
    }

    @Test
    public void should_send_string_and_await_string() {
        kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .send("should_send_string_and_await_string")
                .toTopic("integration-test");

        String record = kafkaSuite
                .clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .await().latestOne()
                .fromTopic("integration-test")
                .toString();

        assertThat(record).isEqualTo("should_send_string_and_await_string");
    }

    @Test
    public void should_send_json_from_file_and_await_JsonObject() {
        kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .send(new ResourceFile("kafka/test-suite.json"))
                .toTopic("integration-test");

        JsonObject record = kafkaSuite
                .await().latestOne()
                .fromTopic("integration-test")
                .toJsonObject();

        assertThat(record.getString("name")).isEqualTo("jack");
        assertThat(record.getInteger("age")).isEqualTo(25);
    }

    @Test
    public void should_send_json_from_file_and_await_JsonArray() {
        kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .send(new ResourceFile("kafka/test-suite-array.json"))
                .toTopic("integration-test");

        JSONArray record = kafkaSuite
                .await().latestOne()
                .fromTopic("integration-test")
                .toJsonArray();

        assertThat(record.size()).isEqualTo(2);
        assertThat(record.getObject(0, Person.class).getName()).isEqualTo("jack");
        assertThat(record.getObject(1, Person.class).getAge()).isEqualTo(23);
    }

    @Test
    public void should_send_json_from_file_and_await_java_object() {
        kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .send(new ResourceFile("kafka/test-suite.json"))
                .toTopic("integration-test");

        Person record = kafkaSuite
                .await().latestOne()
                .fromTopic("integration-test")
                .toJavaObject(Person.class);

        assertThat(record.getName()).isEqualTo("jack");
        assertThat(record.getAge()).isEqualTo(25);
    }

    @Test
    public void should_send_multi_message_and_receive_multi() {
        kafkaSuite.send("{\"name\": \"jack\", \"age\": 25}")
                .toTopic("integration-test");

        kafkaSuite.send("{\"name\": \"tony\", \"age\": 20}")
                .toTopic("integration-test");

        DSLAwaitMultiRecordsStep records = kafkaSuite
                .await().multi()
                .fromTopic("integration-test");

        Person jack = records.fetchSecondLastOne().toJavaObject(Person.class);
        Person tony = records.fetchLastOne().toJavaObject(Person.class);

        assertThat(jack.getName()).isEqualTo("jack");
        assertThat(jack.getAge()).isEqualTo(25);
        assertThat(tony.getName()).isEqualTo("tony");
        assertThat(tony.getAge()).isEqualTo(20);
    }

    @Test
    public void should_send_empty_file_await_empty_string() {
        kafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .send(new ResourceFile("kafka/test-empty"))
                .toTopic("integration-test");

        String record = kafkaSuite
                .await().latestOne()
                .fromTopic("integration-test")
                .toString();

        assertThat(record).isEqualTo("");
    }

    @Test
    public void should_send_message_to_ssl_kafka_and_await() {
        sslKafkaSuite
                .send("should_send_message_to_ssl_kafka_and_await")
                .toTopic("integration-test");

        String record = sslKafkaSuite
                .await().latestOne()
                .fromTopic("integration-test")
                .toString();

        assertThat(record).isEqualTo("should_send_message_to_ssl_kafka_and_await");
    }

    @Test
    public void should_read_topic_of_items_and_config_file() {
        sslKafkaSuite
                .send("should_read_topic_of_items_and_config_file")
                .toTopic()
                .ofConfig("INTEGRATION-TEST");

        String record = sslKafkaSuite
                .await().latestOne()
                .fromTopic().ofConfig("cloud.yml", "INTEGRATION-TEST")
                .toString();

        assertThat(record).isEqualTo("should_read_topic_of_items_and_config_file");
    }

    @Test
    public void should_await_multi_records() {
        sslKafkaSuite
                .send("first record")
                .toTopic()
                .ofConfig("INTEGRATION-TEST");

        sslKafkaSuite
                .send("second record")
                .toTopic()
                .ofConfig("INTEGRATION-TEST");

        List<String> records = sslKafkaSuite
                .await().multi()
                .fromTopic()
                .ofConfig("INTEGRATION-TEST")
                .toList();

        assertThat(records).containsSequence("first record", "second record");
    }

    @Test
    public void should_not_throw_exception_given_have_loaded_ssl_suite_with_clusterIps_and_noSsl() {
        sslKafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .noSsl()
                .send("no ssl")
                .toTopic("integration-test");

        sslKafkaSuite.clusterIps("172.20.10.120:9092,172.20.10.152:9092,172.20.10.171:9092")
                .noSsl()
                .await()
                .fromTopic("integration-test");
    }

    @Ignore
    @Test(expected = SendTimeoutException.class)
    public void should_throw_SendTimeoutException_after_10s_given_wrong_ip_or_ssl_certificate_failed() {
        kafkaSuite.clusterIps("172.20.10.142:9091,172.20.10.143:9091,172.20.10.144:9091")
                .send("should_throw_SendTimeoutException_after_10s_given_wrong_ip_or_ssl_certificate_failed")
                .toTopic("integration-test");

        kafkaSuite.await(3).latestOne().fromTopic("integration-test");
    }

    @Test(expected = AwaitTimeoutException.class)
    public void should_throw_AwaitTimeoutException_when_listen_on_wrong_ip_or_topic() {
        kafkaSuite.await(1).latestOne().fromTopic("nothing");
    }

    @Data
    private static class Person {
        private String name;
        private int age;
    }
}