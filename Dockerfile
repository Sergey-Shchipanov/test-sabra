FROM adoptopenjdk/openjdk11:latest
#COPY ./build/libs /home/test-sabra/
ADD . /usr/local/test-sabra
RUN cd /usr/local/test-sabra && gradle build && gradle jar
CMD ["java","-jar","usr/local/test-sabra/build/libs/test-sabra-0.0.1-SNAPSHOT.jar",  "-Dsabra.elastic.cluster.ip", "localhost", "-Dsabra.elastic.cluster.port", "9200"]