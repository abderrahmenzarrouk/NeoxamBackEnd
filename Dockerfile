FROM openjdk:17
WORKDIR /app
COPY target/neoxamBack-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8083
CMD ["java", "-jar", "neoxamBack-0.0.1-SNAPSHOT.jar"]
