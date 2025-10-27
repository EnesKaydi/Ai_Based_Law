# 🐳 AI Tabanlı Hukuk Sistemi - Java Spring Boot Backend Dockerfile
# Basit single-stage build (JAR zaten hazır)

FROM amazoncorretto:17-alpine-jdk

# Set working directory
WORKDIR /app

# Create non-root user for security (alpine için)
RUN addgroup -g 1001 springboot && \
    adduser -D -u 1001 -G springboot springuser

# Install curl for health check (alpine için)
RUN apk add --no-cache curl

# Create directories
RUN mkdir -p /app/logs && \
    chown -R springuser:springboot /app

# Copy JAR file (hazır JAR dosyasını kopyala)
COPY target/ai-hukuk-backend.jar /app/app.jar

# Change ownership to springuser
RUN chown springuser:springboot /app/app.jar

# Switch to non-root user
USER springuser

# Expose port
EXPOSE 8000

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8000/v1/health || exit 1

# JVM optimization for container environment
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseContainerSupport"

# Start the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

# Metadata
LABEL maintainer="AI Hukuk Sistemi Team"
LABEL description="AI Tabanlı Hukuk Sistemi Backend API - Java Spring Boot"
LABEL version="1.0.0"
LABEL java.version="17"
LABEL spring.boot.version="3.1.5"

# Volume for logs
VOLUME ["/app/logs"]