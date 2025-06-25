package com.t1.cardio.card.model;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.PreUpdate;

@Entity
public class CardGenerationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    @Column(length = 5000)
    private String initialPrompt;

    @Column(length = 5000)
    private String generatedPrompt;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CardGenerationStatus status;

    @Column(length = 5000)
    private String errorMessage;

    @ElementCollection
    @CollectionTable(name = "image_properties")
    @MapKeyColumn(name = "property_name")
    @Column(name = "property_value")
    private Map<String, Float> imageProperties;

    private Integer retryCount = 0;


    public CardGenerationTask() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = CardGenerationStatus.INITIALIZED;
    }

    public CardGenerationTask(String initialPrompt) {
        this();
        this.initialPrompt = initialPrompt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getInitialPrompt() {
        return initialPrompt;
    }

    public void setInitialPrompt(String initialPrompt) {
        this.initialPrompt = initialPrompt;
    }

    public CardGenerationStatus getStatus() {
        return status;
    }

    public void setStatus(CardGenerationStatus status) {
        this.status = status;
    }

    public String getGeneratedPrompt() {
        return generatedPrompt;
    }

    public void setGeneratedPrompt(String generatedPrompt) {
        this.generatedPrompt = generatedPrompt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Float> getImageProperties() {
        return imageProperties;
    }

    public void setImageProperties(Map<String, Float> imageProperties) {
        this.imageProperties = imageProperties;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
