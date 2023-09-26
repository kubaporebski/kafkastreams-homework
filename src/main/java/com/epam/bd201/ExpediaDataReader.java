package com.epam.bd201;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;

import java.util.*;

public class ExpediaDataReader implements Runnable {

    final String PATH = Objects.toString(System.getenv("EXPEDIA_DATA"), "");

    @Override
    public void run() {

        Producer<String, String> producer = createProducer();
        try {



            for (int i = 0; i < 100; i++) {
                String data = String.format("Message #%d with data [%.6f]", i, Math.random());
                producer.send(new ProducerRecord<>(AppConfig.INPUT_TOPIC_NAME, data));

                Thread.sleep(333);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            producer.close();
        }
    }

    private static Producer<String, String> createProducer() {
        Properties kafkaProps = AppConfig.getKafkaProperties();
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG));
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "expedia-data-producer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
}
