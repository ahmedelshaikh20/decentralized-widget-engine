FROM gradle:8.14-jdk17 AS builder

WORKDIR /workspace

COPY settings.gradle build.gradle ./
COPY shared ./shared
COPY widget-gateway ./widget-gateway

RUN gradle :widget-gateway:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /workspace/widget-gateway/build/libs/*.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]