package com.t1.cardio.card.model;


public enum CardGenerationStatus {
    INITIALIZED,
    PENDING_PROMPT,
    GENERATING_PROMPT,
    PENDING_IMAGE,
    GENERATING_IMAGE,
    PENDING_ANALYSIS,
    ANALYZING_PROPERTIES,
    COMPLETED,
    FAILED
}
