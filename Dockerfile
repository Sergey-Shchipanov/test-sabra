FROM adoptopenjdk/openjdk11:latest
ADD . /usr/local/test-sabra
ENTRYPOINT ["java","-jar","usr/local/test-sabra/test-sabra-0.0.1-SNAPSHOT.jar", "elasticsearch", "9300"]