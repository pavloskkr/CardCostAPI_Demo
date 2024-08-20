FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/cardcostapi.jar cardcostapi.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "cardcostapi.jar"]
