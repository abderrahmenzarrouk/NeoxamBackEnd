FROM maven:3.9.0-eclipse-temurin-17 as build
WORKDIR /app
COPY . .
RUN mvn clean install


FROM openjdk:17
WORKDIR /app
COPY ./neoxamBack-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8083
CMD ["java", "-jar", "neoxamBack-0.0.1-SNAPSHOT.jar"]
