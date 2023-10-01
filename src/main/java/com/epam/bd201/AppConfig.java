package com.epam.bd201;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Single class for storing all the configuration.
 *
 * In general, this config is read from environment variables. The constants' names are self-explanatory.
 */
public class AppConfig {

    private static final Properties streamProperties = new Properties();

    public static final String INPUT_TOPIC_NAME = "expedia";

    public static final String OUTPUT_TOPIC_NAME = "expedia_ext";

    public static final String BOOTSTRAP_SERVER = Objects.toString(System.getenv("BOOTSTRAP_SERVER"), "localhost:9092");

    public static final String SCHEMA_REGISTRY_URL = Objects.toString(System.getenv("SCHEMA_REGISTRY_URL"), "http://localhost:8081");

    /** Source GCP bucket name */
    public static final String SOURCE_BUCKET_NAME = Objects.toString(System.getenv("BUCKET_NAME"), "storage-bucket-large-hedgehog");

    /** Directory within bucket name where is Expedia data */
    public static final String SOURCE_DATA_DIR = Objects.toString(System.getenv("DATA_DIR"), "m12kafkastreams/topics/expedia/");

    static {
        streamProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "jporebski-kafkastreams");
        streamProperties.put(StreamsConfig.CLIENT_ID_CONFIG, "jporebski-kafkastreams-client");
        streamProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        streamProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        streamProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        streamProperties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);
    }

    public static Properties getStreamProperties() {
        return streamProperties;
    }

    public static Map<String, String> getSchemaRegistryConfig() {
        return Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConfig.SCHEMA_REGISTRY_URL);
    }
}
