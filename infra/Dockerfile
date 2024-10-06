# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file from the parent directory
COPY ../pom.xml .

# Download all dependencies
RUN mvn dependency:go-offline -B

# Copy the project source
COPY ../src ./src

# Package the application
RUN mvn package

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/url-shortener-0.0.1-jar-with-dependencies.jar ./app.jar

# Copy the documentation.yaml file
COPY --from=build /app/src/main/resources/documentation.yaml ./documentation.yaml

# Expose the port the app runs on
EXPOSE 8080

# Set the PORT environment variable
ENV PORT=8080

# Run the jar file
CMD ["java", "-jar", "app.jar"]