FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /workspace
COPY . .
RUN ./mvnw clean package -DskipTests && \
    cp target/*.jar app.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app.jar .
ENTRYPOINT ["java", "-jar", "app.jar"]