# ==========================
# Stage 1: Build ứng dụng
# ==========================
FROM maven:3.9.11-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom trước để tận dụng cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source
COPY src ./src

# Build
RUN mvn clean package -DskipTests


# ==========================
# Stage 2: Runtime
# ==========================
FROM tomcat:10.1.52-jdk17-temurin

COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080