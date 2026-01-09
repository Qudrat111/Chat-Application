FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

# Install curl for healthcheck
RUN apk add --no-cache curl

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
