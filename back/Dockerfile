# Etapa 1: Build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto que usará tu app (ajusta si es necesario)
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
