FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup

COPY target/cardio.gencard-0.0.1-SNAPSHOT.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8084
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
