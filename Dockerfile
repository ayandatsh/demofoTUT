FROM eclipse-temurin:17-alpine
COPY target/app.jar /app.jar
# This is the port that your javalin application will listen on
EXPOSE 5050
CMD ["java", "-jar", "/app.jar"]
