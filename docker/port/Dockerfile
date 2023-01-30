# Download the build artifact and add to this container
FROM eclipse-temurin:17-jre-focal@sha256:8c7457141fd2ac90e86f86d1e4a2ce4c923e4117716d45226e60da3e31616e0d as Port

MAINTAINER Takenote developer Kurtis Whiteman <kurtiswhiteman@gmail.com>

# Add Spring Boot app.jar to Container
COPY backend/target/takenote*.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
