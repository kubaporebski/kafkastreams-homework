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

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for reading data from a GCP bucket and storing it into a Kafka topic.
 *
 * This class behaves like a Kafka connector, hence the name.
 */
public class GCPSourceConnectorSimulator implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GCPSourceConnectorSimulator.class);

    /** Regex for extracting a partition number from an AVRO file path */
    private final Pattern rePartitionNr = Pattern.compile(".+partition=([0-9]+).+");

    /** Reader of AVRO rows contained in a single AVRO file */
    private final DatumReader<GenericRecord> expediaDatumReader = ExpediaManagement.getDatumReader();

    @Override
    public void run() {

        logger.info(String.format("Starting data retrieval from a GCP bucket: gs://%s/%s \r\n",
                AppConfig.SOURCE_BUCKET_NAME, AppConfig.SOURCE_DATA_DIR));
        logger.info(String.format("Destination Kafka topic: %s \r\n", AppConfig.INPUT_TOPIC_NAME));

        try (Producer<Integer, Object> producer = createProducer();
             Storage storage = StorageOptions.getDefaultInstance().getService()) {

                Page<Blob> blobs = storage.list(AppConfig.SOURCE_BUCKET_NAME, Storage.BlobListOption.prefix(AppConfig.SOURCE_DATA_DIR));
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

                    // start reading data as a stream of AVRO records
                    logger.info("Sending the file in parts to the Kafka topic");
                    SeekableByteArrayInput sin = new SeekableByteArrayInput(blobContents);
                    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(sin, expediaDatumReader);
                    while (dataFileReader.hasNext()) {

                        GenericRecord row = dataFileReader.next();

                        // when record is read, send it to a kafka topic in specified partition
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
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", AppConfig.SCHEMA_REGISTRY_URL);
        return new KafkaProducer<>(props);
    }
}
