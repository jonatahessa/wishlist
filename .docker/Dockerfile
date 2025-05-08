ARG CACHEBUST=1

FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /workspace

# Copia primeiro os arquivos do Maven Wrapper
COPY ../mvnw .
COPY ../.mvn .mvn
COPY ../pom.xml .

# Baixa as dependências
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline -B

# Copia o código fonte
COPY ../src src

# Executa o build
RUN ./mvnw clean package -DskipTests && \
    mv target/*.jar app.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app.jar .
ENTRYPOINT ["java", "-jar", "app.jar"]