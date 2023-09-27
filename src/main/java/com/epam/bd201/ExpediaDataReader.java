package com.epam.bd201;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpediaDataReader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ExpediaDataReader.class);

    private final String BUCKET_NAME = Objects.toString(System.getenv("BUCKET_NAME"), "storage-bucket-large-hedgehog");

    private final String DATA_DIR = Objects.toString(System.getenv("DATA_DIR"), "m12kafkastreams/topics/expedia/");

    private final Pattern rePartitionNr = Pattern.compile(".+partition=([0-9]+).+");

    private final DatumReader<GenericRecord> expediaDatumReader = Expedia.getDatumReader();

    @Override
    public void run() {

        logger.info(String.format("Starting data retrieval from a GCP bucket: gs://%s/%s \r\n", BUCKET_NAME, DATA_DIR));
        logger.info(String.format("Destination Kafka topic: %s \r\n", AppConfig.INPUT_TOPIC_NAME));

        try (Producer<Integer, Object> producer = createProducer();
             Storage storage = StorageOptions.getDefaultInstance().getService()) {

                Page<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(DATA_DIR));
                for (Blob file : blobs.iterateAll()) {

                    String blobFileName = file.getName();
                    logger.info(String.format("The file: %s. Retrieving data from GCP... \r\n", blobFileName));

                    // get a key which is a partition number
                    Matcher matcher = rePartitionNr.matcher(blobFileName);
                    int partitionNr = matcher.matches() ? Integer.parseInt(matcher.group(1)) : 1;

                    // get a value which is just a byte array
                    byte[] blobContents = file.getContent();
                    if (blobContents == null)
                        throw new RuntimeException(String.format("Contents of the file %s are null somehow", blobFileName));

                    logger.info("Sending the file in parts to the Kafka topic");
                    SeekableByteArrayInput sin = new SeekableByteArrayInput(blobContents);
                    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(sin, expediaDatumReader);
                    while (dataFileReader.hasNext()) {

                        GenericRecord row = dataFileReader.next();

                        logger.debug(String.format("Sending row %d/%d to the Kafka topic", dataFileReader.tell(), blobContents.length));
                        producer.send(new ProducerRecord<>(AppConfig.INPUT_TOPIC_NAME, partitionNr, row));
                    }
                    logger.info("Sending the file is done");

                    Thread.sleep(100);
                }

                logger.info("All data sent. Done");
        } catch (Exception e) {
            logger.error("Error during data reading", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Creating of a producer to write to the input Kafka stream.
     *
     * @return Kafka producer object
     */
    private static Producer<Integer, Object> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BOOTSTRAP_SERVER);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "expedia-data-producer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", AppConfig.SCHEMA_REGISTRY_URL);
        return new KafkaProducer<>(props);
    }
}
