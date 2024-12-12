FROM openjdk:17-jdk-slim

# Working directory in the container
WORKDIR /app

# Copying the jar file into the container at /app
COPY target/crypto-history-0.0.1-SNAPSHOT.jar /app/crypto-history-0.0.1-SNAPSHOT.jar

# Running the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/crypto-history-0.0.1-SNAPSHOT.jar"]