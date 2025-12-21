# ---- Build stage ----
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# Copy entire backend (safe for multi-module Gradle)
COPY . .

# Build only widget-gateway
RUN ./gradlew :widget-gateway:build -x test --no-daemon


# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the fat jar
COPY --from=builder /app/widget-gateway/build/libs/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","app.jar"]
