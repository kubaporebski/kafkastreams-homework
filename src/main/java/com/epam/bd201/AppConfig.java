package com.epam.bd201;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Objects;
import java.util.Properties;

public class AppConfig {

    private static final Properties kafkaProperties = new Properties();

    public static final String INPUT_TOPIC_NAME = "expedia";

    public static final String OUTPUT_TOPIC_NAME = "expedia_ext";

    public static final String BOOTSTRAP_SERVER = Objects.toString(System.getenv("BOOTSTRAP_SERVER"), "kafka:9092");

    public static final String SCHEMA_REGISTRY_URL = Objects.toString(System.getenv("SCHEMA_REGISTRY_URL"), "http://schema-registry:8081");

    static {
        kafkaProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "jporebski-kafkastreams");
        kafkaProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        kafkaProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        kafkaProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        kafkaProperties.put("schema.registry.url", SCHEMA_REGISTRY_URL);
    }

    public static Properties getKafkaProperties() {
        return kafkaProperties;
    }
}
