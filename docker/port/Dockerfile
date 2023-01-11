# Download the build artifact and add to this container
FROM eclipse-temurin:17-jre-focal@sha256:8c7457141fd2ac90e86f86d1e4a2ce4c923e4117716d45226e60da3e31616e0d as Port

MAINTAINER Takenote developer Kurtis Whiteman <kurtiswhiteman@gmail.com>

# Add Spring Boot app.jar to Container
COPY "backend*.jar" app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre-focal@sha256:8c7457141fd2ac90e86f86d1e4a2ce4c923e4117716d45226e60da3e31616e0d
COPY --from=Port dependencies/ ./
RUN true
COPY --from=Port snapshot-dependencies/ ./
RUN true
COPY --from=Port spring-boot-loader/ ./
RUN true
COPY --from=Port application/ ./

ENV JAVA_TOOL_OPTIONS="-Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.ssl=false \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.port=15000 \
-Dcom.sun.management.jmxremote.rmi.port=15000 \
-Dcom.sun.management.jmxremote.local.only=false \
-Djava.rmi.server.hostname=127.0.0.1 \
-XX:MaxRAMPercentage=90"

# Set local timezone for logs
RUN ln -sf /usr/share/zoneinfo/Australia/Sydney /etc/localtime && \
    echo "Australia/Sydney" > /etc/timezone

# Fire up our Spring Boot app by default
ENTRYPOINT ["sh", "-c", "java org.springframework.boot.loader.JarLauncher"]
