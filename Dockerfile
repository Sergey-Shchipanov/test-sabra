FROM adoptopenjdk/openjdk11:latest
RUN wget https://services.gradle.org/distributions/gradle-5.0-bin.zip -P /tmp
RUN sudo unzip -d /opt/gradle /tmp/gradle-*.zip
ADD . /usr/local/test-sabra
RUN cd /usr/local/test-sabra && gradle build && gradle jar
CMD ["java","-jar","usr/local/test-sabra/build/libs/test-sabra-0.0.1-SNAPSHOT.jar",  "-Dsabra.elastic.cluster.ip", "localhost", "-Dsabra.elastic.cluster.port", "9200"]