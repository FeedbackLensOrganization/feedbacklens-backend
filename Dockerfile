# ---------- Build Stage ----------
FROM gradle:8-jdk-21-and-22-alpine AS builder
WORKDIR /app

# nur dependencies zunächst kopieren → bessere Build-Caching Zeit
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew .
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# restliches projekt kopieren
COPY . .
RUN ./gradlew bootJar --no-daemon

# ---------- Runtime Stage ----------
FROM eclipse-temurin:23-jre-alpine-3.21
WORKDIR /app

# Sicherheit – non root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# JAR übernehmen
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 9000

# Healthcheck – wichtig für Docker Swarm / k8s / Loadbalancer
HEALTHCHECK --interval=30s --timeout=5s --retries=5 \
  CMD wget -qO- http://localhost:9000/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]