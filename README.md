# Kafka Streams homework

## Configuration
* I didn't use Kafka Connect, I wrote my own connector-like class which is reading data from a GCP bucket and storing it into a Kafka topic.
* You will need to specify __following environment variables__ for connecting to a GCP bucket:
  * `BUCKET_NAME` - name of an existing GCP bucket, where homework data is already uploaded,
  * `DATA_DIR` - path to `expedia` topic data (e.g. _m12kafkastreams/topics/expedia/_). This folder is expected to have `partition=n` subfolders (where _n_ is some number),
  * `GOOGLE_APPLICATION_CREDENTIALS` - path to a GCP Service Account key file. It is required to retrieve data from a GCP bucket.
* Another useful environment variables used by the app:
  * `BOOTSTRAP_SERVER` - location of a kafka cluster, e.g. `kafka:9092`,
  * `SCHEMA_REGISTRY_URL` - full URL of a Schema Registry, e.g. `http://localhost:8081`.

## Steps to recreate
* Build the application using `mvn package`.
* Create docker image using `Dockerfile` in the root directory of this project.

### Running locally
* Make sure you have Zookeeper, Kafka, and Confluent Schema Registry running already.
```
docker run --rm --name qstreams --network host  -v /your/google/service-account/key/file.json:/opt/google.key -e GOOGLE_APPLICATION_CREDENTIALS=/opt/google.key -e GOOGLE_APPLICATION_CREDENTIALS=/opt/google.key -it [dockerimagename]
```

### Running in the cloud
* Push to Kubernetes.
* Run it in the Cloud.

