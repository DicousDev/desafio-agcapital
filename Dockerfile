FROM eclipse-temurin:17-alpine

WORKDIR /app

COPY target/*.jar .

EXPOSE 8080

CMD ["java", "-jar", "agsistemas-0.0.1-SNAPSHOT.jar"]