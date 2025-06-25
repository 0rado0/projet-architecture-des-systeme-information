package com.t1.cardio.card.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.t1.cardio.card.model.CardGenerationStatus;
import com.t1.cardio.card.model.CardGenerationTask;

@Service
public class GenCardService {
    private static final Logger LOGGER = Logger.getLogger(GenCardService.class.getName());

    @Autowired
    private CardGenerationTaskRepository taskRepository;
    
    @Autowired
    private PromptGenerationService promptService;
    
    @Autowired
    private ImageGenerationService imageService;
    
    @Autowired
    private PropertyAnalysisService analysisService;

    /**
     * Crée une nouvelle tâche de génération de carte
     */
    @Transactional
    public CardGenerationTask createCardGenerationTask(String initialPrompt) {
        CardGenerationTask task = new CardGenerationTask(initialPrompt);
        task.setStatus(CardGenerationStatus.PENDING_PROMPT);
        return taskRepository.save(task);
    }

    /**
     * Traite toutes les tâches en attente en une seule méthode
     */
    @Transactional
    public void processAllPendingTasks() {
        processPromptGenerationTasks();
        processImageGenerationTasks();
        processPropertyAnalysisTasks();
    }

    /**
     * Traite les tâches en attente de génération de prompt
     */
    @Transactional
    public void processPromptGenerationTasks() {
        List<CardGenerationTask> tasks = taskRepository.findByStatus(CardGenerationStatus.PENDING_PROMPT);

        for (CardGenerationTask task : tasks) {
            try {
                task.setStatus(CardGenerationStatus.GENERATING_PROMPT);
                taskRepository.save(task);

                String generatedPrompt = promptService.generatePrompt(task.getInitialPrompt());

                task.setGeneratedPrompt(generatedPrompt);
                task.setStatus(CardGenerationStatus.PENDING_IMAGE);
                taskRepository.save(task);
                
                log("Prompt généré pour la tâche " + task.getId());
            } catch (Exception e) {
                logError("Erreur lors de la génération du prompt", e);
                handleTaskError(task, e);
            }
        }
    }

    /**
     * Traite les tâches en attente de génération d'image
     */
    @Transactional
    public void processImageGenerationTasks() {
        List<CardGenerationTask> tasks = taskRepository.findByStatus(CardGenerationStatus.PENDING_IMAGE);

        for (CardGenerationTask task : tasks) {
            try {
                task.setStatus(CardGenerationStatus.GENERATING_IMAGE);
                taskRepository.save(task);

                String imageUrl = imageService.generateImage(task.getGeneratedPrompt());

                task.setImageUrl(imageUrl);
                task.setStatus(CardGenerationStatus.PENDING_ANALYSIS);
                taskRepository.save(task);
                
                log("Image générée pour la tâche " + task.getId());
            } catch (Exception e) {
                logError("Erreur lors de la génération de l'image", e);
                handleTaskError(task, e);
            }
        }
    }

    /**
     * Traite les tâches en attente d'analyse des propriétés
     */
    @Transactional
    public void processPropertyAnalysisTasks() {
        List<CardGenerationTask> tasks = taskRepository.findByStatus(CardGenerationStatus.PENDING_ANALYSIS);

        for (CardGenerationTask task : tasks) {
            try {
                task.setStatus(CardGenerationStatus.ANALYZING_PROPERTIES);
                taskRepository.save(task);

                Map<String, Float> properties = analysisService.analyzeImageProperties(task.getImageUrl());

                task.setImageProperties(properties);
                task.setStatus(CardGenerationStatus.COMPLETED);
                taskRepository.save(task);
                
                log("Analyse terminée pour la tâche " + task.getId());
            } catch (Exception e) {
                logError("Erreur lors de l'analyse des propriétés", e);
                handleTaskError(task, e);
            }
        }
    }

    /**
     * Méthode d'aide pour la journalisation
     */
    private void log(String message) {
        LOGGER.info(message);
    }
    
    /**
     * Méthode d'aide pour la journalisation des erreurs
     */
    private void logError(String message, Exception e) {
        LOGGER.severe(message + ": " + e.getMessage());
    }

    /**
     * Gère les erreurs lors du traitement des tâches
     */
    private void handleTaskError(CardGenerationTask task, Exception e) {
        task.setErrorMessage(e.getMessage());
        task.setRetryCount(task.getRetryCount() + 1);

        // Si trop de tentatives, marquer comme échoué
        if (task.getRetryCount() >= 3) {
            task.setStatus(CardGenerationStatus.FAILED);
            log("Tâche échouée définitivement: " + task.getId());
        }

        taskRepository.save(task);
    }

    /**
     * Vérifie et récupère les tâches bloquées
     */
    @Transactional
    public void recoverStuckTasks() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        
        // Structure simplifiée pour récupérer les tâches bloquées
        resetStuckTasks(CardGenerationStatus.GENERATING_PROMPT, CardGenerationStatus.PENDING_PROMPT, threshold);
        resetStuckTasks(CardGenerationStatus.GENERATING_IMAGE, CardGenerationStatus.PENDING_IMAGE, threshold);
        resetStuckTasks(CardGenerationStatus.ANALYZING_PROPERTIES, CardGenerationStatus.PENDING_ANALYSIS, threshold);
    }
    
    /**
     * Réinitialise les tâches bloquées d'un statut spécifique
     */
    private void resetStuckTasks(CardGenerationStatus currentStatus, CardGenerationStatus resetStatus, LocalDateTime threshold) {
        List<CardGenerationTask> stuckTasks = 
                taskRepository.findByStatusAndUpdatedAtBefore(currentStatus, threshold);
        
        for (CardGenerationTask task : stuckTasks) {
            task.setStatus(resetStatus);
            taskRepository.save(task);
            log("Tâche bloquée réinitialisée: " + task.getId() + " (de " + currentStatus + " à " + resetStatus + ")");
        }
    }
    
    /**
     * Récupère une tâche par son ID
     */
    public CardGenerationTask getTaskById(Integer taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }
}