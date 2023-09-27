package com.epam.bd201;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KStreamsApplication {

    private static final Logger logger = LoggerFactory.getLogger(KStreamsApplication.class);

    public static void main(String[] args) {

        logger.info("Running KStreamsApplication... ");

        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> inputRecords = builder.stream(AppConfig.INPUT_TOPIC_NAME, Consumed.with(Serdes.String(), Serdes.String()));

        //Transform your records here
        //input_records.map();
        // input_records.map(KStreamsApplication::beanMapper);

        inputRecords.to(AppConfig.OUTPUT_TOPIC_NAME);

        final Topology topology = builder.build();
        logger.info(topology.describe().toString());

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

        ExecutorService ste = Executors.newSingleThreadExecutor();

        try {
            // 1. run streaming operations
            logger.info("Starting streaming operations");
            streams.start();

            // 2. and then start copying expedia data
            logger.info("Starting reading data from a GCP bucket");
            ste.submit(new ExpediaDataReader());

            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        } finally {
            ste.shutdownNow();
        }
        System.exit(0);
    }

}
