package com.t1.cardio.card.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.t1.cardio.card.controller.GenCardService;
import com.t1.cardio.card.model.CardGenerationTask;

@RestController
public class GencardRestController {

    @Autowired
    private GenCardService genCardService;

    @RequestMapping(method=RequestMethod.GET, value="/gencard")
    public ResponseEntity<Map<String, Object>> generateCard(@RequestParam("prompt") String prompt) {
        CardGenerationTask task = genCardService.createCardGenerationTask(prompt);
        
        Map<String, Object> response = new HashMap<>();
        response.put("taskId", task.getId());
        response.put("status", task.getStatus().toString());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/gencard/{taskId}")
    public ResponseEntity<Map<String, Object>> getTaskStatus(@PathVariable("taskId") Integer taskId) {
        CardGenerationTask task = genCardService.getTaskById(taskId);
        
        if (task == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Tâche non trouvée");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("taskId", task.getId());
        response.put("status", task.getStatus().toString());
        response.put("createdAt", task.getCreatedAt());
        response.put("updatedAt", task.getUpdatedAt());
        response.put("initialPrompt", task.getInitialPrompt());
        
        // Ajouter des informations supplémentaires en fonction de l'état de la tâche
        if (task.getGeneratedPrompt() != null) {
            response.put("generatedPrompt", task.getGeneratedPrompt());
        }
        
        if (task.getImageUrl() != null) {
            response.put("imageUrl", task.getImageUrl());
        }
        
        if (task.getImageProperties() != null) {
            response.put("imageProperties", task.getImageProperties());
        }
        
        if (task.getErrorMessage() != null) {
            response.put("errorMessage", task.getErrorMessage());
            response.put("retryCount", task.getRetryCount());
        }
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}