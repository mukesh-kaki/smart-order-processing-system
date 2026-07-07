FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

COPY . .

RUN ./gradlew :payment-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/payment-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]