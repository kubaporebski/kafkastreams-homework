FROM openjdk:8-jre
ADD target/m12_kafkastreams_jvm_azure-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
ADD service-account-key.json service-account-key.json
ENV GOOGLE_APPLICATION_CREDENTIALS /service-account-key.json
ENTRYPOINT ["java","-cp","/app.jar","com.epam.bd201.KStreamsApplication"]
