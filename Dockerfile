FROM openjdk:8
ADD target/tpa-test-jar-with-dependencies.jar tpa-test-jar-with-dependencies.jar
ADD src/main/resources/public/files src/main/resources/public/files
ENV APIGEO_TOKEN=iwXSqC80za19L+w7UpixiJZ4EIpTJRf8/8piLIpp67Q=
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tpa-test-jar-with-dependencies.jar"]