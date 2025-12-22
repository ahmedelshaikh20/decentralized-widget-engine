FROM gradle:8.14-jdk17 AS builder

WORKDIR /workspace

COPY settings.gradle build.gradle ./
COPY shared ./shared
COPY widget-processor ./widget-processor

RUN gradle :widget-processor:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /workspace/widget-processor/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]