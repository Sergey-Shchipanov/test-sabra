FROM adoptopenjdk/openjdk11:latest
COPY ./build/libs /home/test-sabra/
CMD ["java","-jar","/home/test-sabra-0.0.1-SNAPSHOT.jar",  "-Dsabra.elastic.cluster.ip", "localhost", "-Dsabra.elastic.cluster.port", "9200"]