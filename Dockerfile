# 1) 빌드 스테이지: Gradle + JDK21
FROM gradle:jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle :user-api:bootJar --no-daemon

# 2) 런타임 스테이지: Java 21 경량 JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/user-api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
