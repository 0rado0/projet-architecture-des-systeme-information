# Stage de build
FROM eclipse-temurin:21-jdk-alpine AS builder

# Installer Maven
RUN apk add --no-cache maven

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests

# Stage de production
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Création d'un utilisateur non-root pour la sécurité
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Copie du JAR depuis le stage de build
COPY --from=builder /app/target/*.jar app.jar

# Changement de propriétaire
RUN chown -R appuser:appgroup /app
USER appuser

# Configuration JVM optimisée pour les conteneurs
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]