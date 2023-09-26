package com.epam.bd201;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class KStreamsApplication {

    public static void main(String[] args) {

        Executors.newSingleThreadExecutor().execute(new ExpediaDataReader());

        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, String> input_records = builder.stream(AppConfig.INPUT_TOPIC_NAME, Consumed.with(Serdes.String(), Serdes.String()));

        //Transform your records here
        //input_records.map();
        // input_records.map(KStreamsApplication::beanMapper);

        input_records.to(AppConfig.OUTPUT_TOPIC_NAME);

        final Topology topology = builder.build();
        System.out.println(topology.describe());

        final KafkaStreams streams = new KafkaStreams(topology, AppConfig.getKafkaProperties());
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

}
