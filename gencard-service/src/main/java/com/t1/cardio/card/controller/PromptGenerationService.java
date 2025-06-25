package com.t1.cardio.card.controller;

import com.t1.cardio.card.utils.CardConstants;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptGenerationService {

    @Autowired
    private HttpRequestService httpRequestService;

    public String generatePrompt(String initialPrompt) throws Exception {

        String promptJsonData = httpRequestService.createPromptRequestJson(initialPrompt);
        var postResponse = httpRequestService.sendPostRequest(CardConstants.PROMPT_REQUEST_URL, promptJsonData);

        JsonNode rootNode = httpRequestService.parseJsonResponse(postResponse.body());
        String requestId = rootNode.path("apiReqId").asText("");

        if (requestId.isEmpty()) {
            throw new Exception("Failed to get request ID");
        }

        String fullPollUrl = CardConstants.PROMPT_POLL_REQUEST_URL + "/" + requestId;
        JsonNode resultat = httpRequestService.pollUntilFinished(fullPollUrl);

        JsonNode resultNode = resultat.path("responsePromptTxt");
        if (resultNode.isMissingNode()) {
            throw new Exception("No result found in the response");
        }

        String resultText = resultNode.asText();
        if (resultText.isEmpty()) {
            throw new Exception("Result text is empty");
        }

        return resultText;
    }
}