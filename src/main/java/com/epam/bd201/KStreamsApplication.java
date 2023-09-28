package com.epam.bd201;

import expedia.ExpediaIn;
import expedia.ExpediaOut;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KStreamsApplication {

    private static final Logger logger = LoggerFactory.getLogger(KStreamsApplication.class);

    public static void main(String[] args) throws Exception{

        logger.info("Running KStreamsApplication... ");

        SpecificAvroSerde<ExpediaIn> serdeExpediaIn = new SpecificAvroSerde<>();
        serdeExpediaIn.configure(AppConfig.getSchemaRegistryConfig(), false);

        SpecificAvroSerde<ExpediaOut> serdeExpediaOut = new SpecificAvroSerde<>();
        serdeExpediaOut.configure(AppConfig.getSchemaRegistryConfig(), false);

        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<Integer, ExpediaIn> inputRecords = builder.stream(AppConfig.INPUT_TOPIC_NAME, Consumed.with(Serdes.Integer(), serdeExpediaIn));
        inputRecords
                .mapValues(Expedia::process)
                .to(AppConfig.OUTPUT_TOPIC_NAME, Produced.with(Serdes.Integer(), serdeExpediaOut));

        final Topology topology = builder.build();
        logger.info(topology.describe().toString());

        final KafkaStreams streams = new KafkaStreams(topology, AppConfig.getStreamProperties());

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        ExecutorService ste = Executors.newSingleThreadExecutor();

        try {
            // 1. run streaming operations
            logger.info("Starting streaming operations");
            streams.start();

            // 2. and then start copying expedia data
            logger.info("Starting reading data from a GCP bucket");
            ste.submit(new ExpediaDataReader()).get();

            logger.info("Finished the task");
        } catch (Throwable e) {
            System.exit(1);
        } finally {
            ste.shutdownNow();
        }
        System.exit(0);
    }

}
