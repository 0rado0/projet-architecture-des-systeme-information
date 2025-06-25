package com.t1.cardio.card.controller;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.t1.cardio.card.model.Card;
import com.t1.cardio.card.model.Card.CardStatus;

public interface CardRepository extends CrudRepository<Card, Integer> {

    // Méthode pour rechercher des carte par titre
    public List<Card> findByName(String name);

    // Méthode pour rechercher des cartes par userId
    public List<Card> findByUserId(int userId);

    // Méthode pour récupérer les cartes sans userId
    public List<Card> findByUserIdIsNull();

    public List<Card> findByIsOnMarket(boolean isOnMarket);
    
    // Méthode pour récupérer les cartes par statut
    public List<Card> findByStatus(CardStatus status);
}
