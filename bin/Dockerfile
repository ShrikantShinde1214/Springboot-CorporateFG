# ===== Stage 1: Build the app =====
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy all project files
COPY . .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Build the application (skip tests to speed it up)
RUN ./mvnw clean package -DskipTests

# ===== Stage 2: Run the app =====
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy only the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
