# Start with a base image containing Java runtime
FROM eclipse-temurin:17-jre-alpine

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Set application's location inside container and data
WORKDIR /app

# Copy the jar file into the container
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables (these will be overridden by Render's environment variables)
ENV SPRING_PROFILES_ACTIVE=prod
ENV DATABASE_URL=""
ENV GOOGLE_CLIENT_ID=""
ENV GOOGLE_CLIENT_SECRET=""
ENV JWT_SECRET=""
ENV JWT_EXPIRATION=86400000

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]