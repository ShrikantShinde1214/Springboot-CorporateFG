# Use official Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory in container
WORKDIR /app

# Copy project files into container
COPY . .

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "target/*.jar"]
