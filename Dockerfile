FROM adoptopenjdk/openjdk11:latest
ADD . /usr/local/test-sabra
ENTRYPOINT ["java","-jar","usr/local/test-sabra/build/libs/test-sabra-0.0.1-SNAPSHOT.jar", "localhost", "9200"]