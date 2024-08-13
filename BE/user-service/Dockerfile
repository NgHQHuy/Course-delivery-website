FROM eclipse-temurin:17-jre-jammy 
COPY ./target/*.jar /app/runner.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]