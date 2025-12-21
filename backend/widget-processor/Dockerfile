# ---- Build stage ----
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# Copy Gradle config first
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle

# Create empty module dirs required by settings.gradle
RUN mkdir -p shared widget-processor widget-gateway

# Copy build files only (dependency cache layer)
COPY shared/build.gradle shared/
COPY widget-processor/build.gradle widget-processor/
COPY widget-gateway/build.gradle widget-gateway/

# Download dependencies
RUN ./gradlew :widget-processor:dependencies --no-daemon

# Copy full source
COPY shared ./shared
COPY widget-processor ./widget-processor

# Build widget-processor
RUN ./gradlew :widget-processor:build -x test --no-daemon


# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/widget-processor/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
