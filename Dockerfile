# --- Etapa 1: Construcción (Build Stage) ---
# Usamos una imagen oficial de Maven (con Java 21) para construir nuestro proyecto
FROM maven:3.9-eclipse-temurin-21 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo el pom.xml para descargar dependencias
# (Esto aprovecha la caché de Docker si no cambiamos las dependencias)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Corremos el comando de build de Maven (igual que en la guía)
# Esto compilará el código y creará el .jar en /app/target/
# ¡¡AÑADIMOS '-DskipTests' PARA EVITAR EL ERROR DE PLACEHOLDER!!
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Run Stage) ---
# Usamos una imagen ligera de Java 21 para *ejecutar* la aplicación
FROM eclipse-temurin:21-jre-alpine

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos SOLO el .jar construido de la etapa 'build'
COPY --from=build /app/target/flight-api-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (el que usa Spring Boot)
EXPOSE 8080

# El comando que se ejecutará cuando el contenedor arranque
# (Este es nuestro "Start Command")
CMD ["java", "-jar", "app.jar"]