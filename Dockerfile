# Start from the base image
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Copy the pom.xml and mvnw files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Set executable permissions for mvnw
RUN chmod +x mvnw

# Copy the rest of your application
COPY src /app/src

# Run the Maven build
RUN ./mvnw clean package

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "neoxamBack-0.0.1-SNAPSHOT.jar"]