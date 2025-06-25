package com.t1.cardio.card.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class EndpointPoller {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final int DEFAULT_INITIAL_DELAY_MS = 5000;
    private static final int DEFAULT_POLLING_INTERVAL_MS = 4000;
    private static final int DEFAULT_MAX_ATTEMPTS = 75;

    public EndpointPoller() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }


    public JsonNode waitForFinishedStatus(String endpoint) throws Exception {
        return pollUntilCondition(
                endpoint,
                jsonNode -> "FINISHED".equals(jsonNode.path("state").asText()),
                DEFAULT_INITIAL_DELAY_MS,
                DEFAULT_POLLING_INTERVAL_MS,
                DEFAULT_MAX_ATTEMPTS
        );
    }


    public JsonNode pollUntilCondition(
            String endpoint,
            Predicate<JsonNode> condition,
            int initialDelayMs,
            int pollingIntervalMs,
            int maxAttempts) throws Exception {

        CompletableFuture<JsonNode> future = new CompletableFuture<>();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        class PollTask implements Runnable {
            private int attemptCount = 0;

            @Override
            public void run() {
                try {
                    attemptCount++;

                    // Effectuer la requête GET
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(endpoint))
                            .GET()
                            .header("Accept", "application/json")
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    // On vérifie si la réponse est OK (code 2xx)
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {

                        JsonNode jsonNode = objectMapper.readTree(response.body());

                        if (condition.test(jsonNode)) {  // condition.test permet de vérifier si le JSON contient "FINISHED"
                            future.complete(jsonNode);
                            executor.shutdown();
                            return;
                        } else {
                            System.out.println("Génération en cours... nouvelle vérification dans " + pollingIntervalMs + "ms (tentative " + attemptCount + "/" + maxAttempts + ")");
                        }
                    } else {
                        System.out.println("Erreur HTTP: " + response.statusCode() + " - " + response.body());
                    }

                    if (attemptCount >= maxAttempts) {
                        future.completeExceptionally(new Exception("Nombre maximum de tentatives atteint (" + maxAttempts + ")"));
                        executor.shutdown();
                        return;
                    }

                    executor.schedule(this, pollingIntervalMs, TimeUnit.MILLISECONDS);

                } catch (Exception e) {
                    future.completeExceptionally(e);
                    executor.shutdown();
                }
            }
        }

        executor.schedule(new PollTask(), initialDelayMs, TimeUnit.MILLISECONDS);

        try {
            return future.get();
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
    }
}