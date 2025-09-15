FROM maven:3.9.6-eclipse-temurin-21 AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivo pom.xml primeiro (para cache das dependências)
COPY pom.xml .

# Baixar dependências (será cacheado se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar o projeto
RUN mvn clean package -DskipTests -B

# Segunda etapa - imagem final menor
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar JAR da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expor porta
EXPOSE 8080

# Comando para executar
ENTRYPOINT ["java", "-jar", "app.jar"]