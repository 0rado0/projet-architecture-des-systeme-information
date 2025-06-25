package com.t1.cardio.card.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class CardGenerationScheduler {

    private static final Logger LOGGER = Logger.getLogger(CardGenerationScheduler.class.getName());

    @Autowired
    private GenCardService genCardService;

    @Scheduled(fixedDelay = 2000)
    public void processPendingTasks() {
        try {
            // Traite tous les types de tâches en attente
            genCardService.processAllPendingTasks();
        } catch (Exception e) {
            log("Erreur lors du traitement des tâches", e);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void recoverStuckTasks() {
        try {
            genCardService.recoverStuckTasks();
        } catch (Exception e) {
            log("Erreur lors de la récupération des tâches bloquées", e);
        }
    }
    
    /**
     * Méthode d'aide pour la journalisation des erreurs
     */
    private void log(String message, Exception e) {
        LOGGER.severe(message + ": " + e.getMessage());
    }
}