package com.t1.cardio.card.utils;

public class CardConstants {
    // Flag du mode debug - active la génération de fausses images
    public static final boolean DEBUG = false;

    public static final String IMAGE_SERVICE_BASE_URL = "http://tp.cpe.fr:8088";

    // URLs pour les prompts
    public static final String PROMPT_REQUEST_URL = IMAGE_SERVICE_BASE_URL +
            (DEBUG ? "/llm-service/fake/prompt/req" : "/llm-service/prompt/req");
    public static final String PROMPT_POLL_REQUEST_URL = IMAGE_SERVICE_BASE_URL + "/llm-service/prompt/req";

    // URLs pour les images
    public static final String IMAGE_REQUEST_URL = IMAGE_SERVICE_BASE_URL +
            (DEBUG ? "/img-service/fake/prompt/req" : "/img-service/prompt/req");
    public static final String IMAGE_POLL_REQUEST_URL = IMAGE_SERVICE_BASE_URL + "/img-service/prompt/req/api";

    public static final String API_TOKEN = "1d4b77b3-7d7b-4ee1-b0ad-ce89a2e2f0be";
}