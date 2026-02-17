# Estágio 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Build do shared-contracts (Dependência local)
WORKDIR /app/shared-contracts
COPY shared-contracts/pom.xml .
COPY shared-contracts/src ./src
RUN mvn clean install -DskipTests

# 2. Build do notification-service
WORKDIR /app/notification-service
COPY notification-service/pom.xml .
# Baixa dependências para aproveitar o cache do Docker
RUN mvn dependency:go-offline

COPY notification-service/src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/notification-service/target/*.jar app.jar

# Configuração de fuso horário (Manaus)
RUN apk add --no-cache tzdata
ENV TZ=America/Manaus

# Porta do serviço
EXPOSE 8082

# Execução com Virtual Threads e limite de memória
ENTRYPOINT ["java", "-Xmx512m", "-Duser.timezone=America/Manaus", "-jar", "app.jar"]