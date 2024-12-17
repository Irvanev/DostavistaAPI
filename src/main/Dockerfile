FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/Dostavista-0.0.1-SNAPSHOT.jar /app/Dostavista-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "Dostavista-0.0.1-SNAPSHOT.jar"]