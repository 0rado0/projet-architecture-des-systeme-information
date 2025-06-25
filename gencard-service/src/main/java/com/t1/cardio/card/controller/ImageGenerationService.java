package com.t1.cardio.card.controller;

import com.t1.cardio.card.utils.CardConstants;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageGenerationService {

    @Autowired
    private HttpRequestService httpRequestService;

    public String generateImage(String prompt) throws Exception {

        String imageJsonData = httpRequestService.createImageRequestJson(prompt);
        var imagePostResponse = httpRequestService.sendPostRequest(CardConstants.IMAGE_REQUEST_URL, imageJsonData);

        String imageRequestId = imagePostResponse.body().trim();
        if (imageRequestId.isEmpty()) {
            throw new Exception("Failed to get image request ID");
        }

        String fullImagePollUrl = CardConstants.IMAGE_POLL_REQUEST_URL + "/" + imageRequestId;
        JsonNode imageResultat = httpRequestService.pollUntilFinished(fullImagePollUrl);

        JsonNode imageResultNode = imageResultat.path("url");
        if (imageResultNode.isMissingNode()) {
            throw new Exception("No image result found in the response");
        }

        String imageResultText = imageResultNode.asText();
        if (imageResultText.isEmpty()) {
            throw new Exception("Image result text is empty");
        }

        return imageResultText;
    }
}