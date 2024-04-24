# Compile
FROM maven:3.8.5-openjdk-17 AS builder

# Create and set the working directory
RUN mkdir app
RUN cd app
WORKDIR /app

# Copy the source code to the container and build the application
COPY pom.xml .
COPY src ./src
RUN mvn clean install package -Dmaven.test.skip

# Run the application
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
