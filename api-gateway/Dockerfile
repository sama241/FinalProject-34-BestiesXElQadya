# Use a lightweight Java image
FROM openjdk:25-ea-4-jdk-oraclelinux9

# Set working directory inside container
WORKDIR /app

# Copy the built jar file into the container
COPY target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose the port 8761
EXPOSE 8761

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]