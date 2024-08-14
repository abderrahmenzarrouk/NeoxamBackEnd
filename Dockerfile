# Use a Maven image to build the application
FROM maven:3.8.6-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . /app

# Build the application
RUN mvn clean package

# Use a Java runtime image to run the application
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/neoxamBack-0.0.1-SNAPSHOT.jar /app/

# Expose the port the application will run on
EXPOSE 8083

# Run the application
CMD ["java", "-jar", "neoxamBack-0.0.1-SNAPSHOT.jar"]
