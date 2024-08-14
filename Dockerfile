FROM maven:3.8.6-openjdk-17
WORKDIR /app
COPY . /app
EXPOSE 8083
CMD ["mvn", "spring-boot:run"]


FROM openjdk:17
WORKDIR /app
COPY ./neoxamBack-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8083
CMD ["java", "-jar", "neoxamBack-0.0.1-SNAPSHOT.jar"]
