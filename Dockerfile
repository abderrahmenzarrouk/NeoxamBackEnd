FROM openjdk:17-jdk
WORKDIR /app
COPY ./target/neoxamBack-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "neoxamBack-0.0.1-SNAPSHOT.jar"]
