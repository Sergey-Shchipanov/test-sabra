FROM adoptopenjdk/openjdk11:latest
ADD . /usr/local/test-sabra
ENV sabra.elastic.ip=localhost
ENV sabra.elastic.port=9200
ENTRYPOINT ["java","-jar","usr/local/test-sabra/build/libs/test-sabra-0.0.1-SNAPSHOT.jar"]