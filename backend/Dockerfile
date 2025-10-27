# 🐳 AI Tabanlı Hukuk Sistemi - Backend Dockerfile

# Base image
FROM node:20-alpine

# Set working directory
WORKDIR /app

# Create non-root user for security
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nodeuser

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production && npm cache clean --force

# Alpine'e curl ekle (root user iken)
RUN apk add --no-cache curl

# Copy source code
COPY src/ ./src/

# Change ownership to nodeuser
RUN chown -R nodeuser:nodejs /app

# Switch to non-root user
USER nodeuser

# Expose port
EXPOSE 8000

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8000/v1/health || exit 1

# Start the application
CMD ["npm", "start"]

# Metadata
LABEL maintainer="AI Hukuk Sistemi Team"
LABEL description="AI Tabanlı Hukuk Sistemi Backend API"
LABEL version="1.0.0"
