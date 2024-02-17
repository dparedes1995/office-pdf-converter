# Usar una imagen base oficial de Maven para construir el proyecto
FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
# Copiar el pom.xml y descargar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline
# Copiar el código fuente del proyecto
COPY src ./src
# Construir la aplicación
RUN mvn package -DskipTests

# Usar una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:17-slim-buster
# Instalar LibreOffice
RUN apt-get update && \
    apt-get install -y libreoffice && \
    apt-get clean
# Definir una variable de entorno para el path de LibreOffice
ENV LIBREOFFICE_PATH=libreoffice
# Copiar el JAR construido desde el paso de construcción
COPY --from=build /app/target/*.jar app.jar
WORKDIR /app
# Puerto en el que se ejecutará la aplicación
EXPOSE 8080
# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
