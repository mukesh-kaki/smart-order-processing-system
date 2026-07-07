FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

COPY . .

RUN ./gradlew :inventory-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/inventory-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]