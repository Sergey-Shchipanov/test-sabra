FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
#ENV JAR_FILE = "/home/abcd666/test-sabra/build/libs/test-sabra-0.0.1-SNAPSHOT.jar"
COPY /home/abcd666/test-sabra/build/libs/test-sabra-0.0.1-SNAPSHOT.jar app.jar
CMD java -Dsabra.elastic.cluster.ip=127.0.0.1 -Dsabra.elastic.cluster.port=9200 -jar /app.jar