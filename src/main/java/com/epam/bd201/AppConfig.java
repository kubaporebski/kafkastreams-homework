package com.epam.bd201;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class AppConfig {

    private final static Properties kafkaProperties = new Properties();

    public static final String INPUT_TOPIC_NAME = "expedia";

    public static final String OUTPUT_TOPIC_NAME = "expedia_ext";

    static {
        kafkaProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "jporebski-kafkastreams");
        kafkaProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        kafkaProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        kafkaProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        //If needed
        //props.put("schema.registry.url", "INSERT_YOUR_SCHEMA_REGISTRY_IP:PORT");
    }

    public static Properties getKafkaProperties() {
        return kafkaProperties;
    }
}
