FROM eclipse-temurin:17-alpine
COPY ./target/app.jar /usr/main/java/weshare/server/app.jar
# This is the port that your javalin application will listen on
EXPOSE 5050
ENTRYPOINT ["java", "-jar", "/app.jar"]
