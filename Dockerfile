FROM eclipse-temurin:17-jre

WORKDIR /reminder-backend
COPY build/libs/*.jar app.jar
COPY src/main/resources /app/resources

EXPOSE 9000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
