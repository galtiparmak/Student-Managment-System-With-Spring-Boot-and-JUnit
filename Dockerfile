FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/student-system.jar /app/student-system.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/student-system.jar"]
