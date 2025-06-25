package com.t1.cardio.card.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.t1.cardio.card.utils.CardConstants;

@Service
public class HttpRequestService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final EndpointPoller poller;


    private final String apiToken = CardConstants.API_TOKEN;
    private static final boolean DEBUG = CardConstants.DEBUG;

    public HttpRequestService() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
        this.poller = new EndpointPoller();
    }

    public HttpResponse<String> sendPostRequest(String url, String jsonData)
            throws IOException, InterruptedException {
        System.out.println("Sending request to: " + url);
        System.out.println("Request body: " + jsonData);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json; charset=utf-8")
                .timeout(Duration.ofSeconds(30));

        if (!DEBUG && apiToken != null && !apiToken.isEmpty()) {
            System.out.println("Adding authorization header");
            requestBuilder.header("Authorization", apiToken);
        }

        HttpRequest postRequest = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("POST Status: " + response.statusCode());
        System.out.println("POST Response: " + response.body());

        return response;
    }


    public String createPromptRequestJson(String promptText) throws IOException {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("promptTxt", promptText);
        requestBody.put("contextTxt", "");
        return objectMapper.writeValueAsString(requestBody);
    }

    public String createImageRequestJson(String promptText) throws IOException {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("promptTxt", promptText);
        return objectMapper.writeValueAsString(requestBody);
    }

    public JsonNode pollUntilFinished(String pollUrl) throws Exception {
        System.out.println("Polling results from: " + pollUrl);
        JsonNode result = poller.waitForFinishedStatus(pollUrl);
        System.out.println("Job terminé avec succès: " + result);
        return result;
    }

    public JsonNode parseJsonResponse(String responseBody) throws IOException {
        return objectMapper.readTree(responseBody);
    }
}