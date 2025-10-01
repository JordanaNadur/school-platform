# Etapa 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar o pom.xml e baixar dependências (cache eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar o código e gerar o jar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final (leve)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiar o jar da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expor a porta
EXPOSE 8080

# Rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
