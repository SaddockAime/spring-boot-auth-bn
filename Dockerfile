# Build stage
FROM maven:3.8.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar
VOLUME /tmp
EXPOSE 8081

# Set environment variables (these will be overridden by Render's environment variables)
ENV SPRING_PROFILES_ACTIVE=prod
ENV DATABASE_URL=""
ENV GOOGLE_CLIENT_ID=""
ENV GOOGLE_CLIENT_SECRET=""
ENV JWT_SECRET=""
ENV JWT_EXPIRATION=86400000

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]