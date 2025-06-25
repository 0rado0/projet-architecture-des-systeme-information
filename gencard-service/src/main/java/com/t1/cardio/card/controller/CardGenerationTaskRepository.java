package com.t1.cardio.card.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.t1.cardio.card.model.CardGenerationStatus;
import com.t1.cardio.card.model.CardGenerationTask;

public interface CardGenerationTaskRepository extends CrudRepository<CardGenerationTask, Integer> {

    List<CardGenerationTask> findByStatus(CardGenerationStatus status);
    
    List<CardGenerationTask> findByStatusAndUpdatedAtBefore(CardGenerationStatus status, LocalDateTime timeThreshold);

    @Query("SELECT t FROM CardGenerationTask t WHERE t.status IN (:statuses) ORDER BY t.createdAt ASC")
    List<CardGenerationTask> findTasksByStatusesSortedByCreationTime(List<CardGenerationStatus> statuses);
}
