FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} test-sabra-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Dsabra.elastic.cluster.ip=127.0.0.1", "-Dsabra.elastic.cluster.port=9200", "-jar","/test-sabra-0.0.1-SNAPSHOT.jar"]