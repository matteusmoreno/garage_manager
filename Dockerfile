FROM eclipse-temurin:21-jdk-alpine

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar o JAR gerado
COPY target/garage-manager-1.0.0-SNAPSHOT-runner.jar app.jar

# Expor a porta
EXPOSE 8080

# Rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
