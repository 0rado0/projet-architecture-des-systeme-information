package com.t1.cardio.shop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.t1.cardio.shop.model.AppUser;
import com.t1.cardio.shop.model.Card;
import com.t1.cardio.shop.model.ShopOrder;
import com.t1.cardio.shop.model.ShopTransaction;
import com.t1.cardio.shop.rest.CardRestClient;
import com.t1.cardio.shop.rest.UserRestClient;


@Service
public class ShopService {
    @Autowired
    private ShopTransactionRepository shopTransactionRepository;

    @Autowired
    private CardRestClient cardRestClient;

    @Autowired
    private UserRestClient userRestClient;

    public List<ShopTransaction> getAllTransactions() {
        return (List<ShopTransaction>) shopTransactionRepository.findAll();
    }

    public List<Card> getCardsOnMarket() {
        List<Card> Cards = cardRestClient.getCard();
    
        List<Card> validMarketCards = new ArrayList<>();
        
        for (Card card : Cards) {
            if (card.isIsOnMarket() && card.getUserId() != null && card.getPrice() > 0) {
                validMarketCards.add(card);
            }
        }

        return validMarketCards;
    }

    public List<Card> getCardsOnMarketByUserId(int userId) {
        return cardRestClient.getCardOnMarketByUserId(userId);
    }

    public boolean sellCard(ShopOrder shopOrder) {
        Integer sellerId = shopOrder.getUserId();
        System.out.println("sellerId = " + sellerId);
        Card card = cardRestClient.getCardByID(shopOrder.getCardId());
        System.out.println("card = " + card);

        if (card == null) {
            return false;
        }
        if (card.getUserId() == null || !Objects.equals(card.getUserId(), sellerId)) {
            return false;
        }
        if (card.isOnMarket()) {
            return false;
        }

        System.out.println("card.getUserId() = " + card.getUserId());
        cardRestClient.setCardOnMarket(shopOrder.getCardId(), true);
        System.out.println("card.getUserId() = " + card.getUserId());
        return true;
    }

    public boolean buyCard(ShopOrder shopOrder) {
        Card card = cardRestClient.getCardByID(shopOrder.getCardId());

        if (card == null) {
            return false;
        }

        Integer buyerId = shopOrder.getUserId();
        Integer sellerId = card.getUserId();

        AppUser buyer = userRestClient.getUserById(buyerId);
        AppUser seller = userRestClient.getUserById(sellerId);

        if (buyer == null || seller == null) {
            return false;
        }

        if (!card.isOnMarket()) {
            return false;
        }

        double price = card.getPrice();

        if (buyer.getFunds() < price) {
            return false;
        }
        
        if (!buyerId.equals(sellerId)) {
            cardRestClient.updateCardUserID(shopOrder.getCardId(), buyerId);
            userRestClient.patchUser(buyerId, buyer.getFunds() - price);
            userRestClient.patchUser(sellerId, seller.getFunds() + price);
        }

        ShopTransaction transaction = new ShopTransaction(
                buyerId,
                sellerId,
                shopOrder.getCardId(),
                price,
                ShopTransaction.Action.BUY,
                null
        );

        cardRestClient.setCardOnMarket(shopOrder.getCardId(), false);
        
        shopTransactionRepository.save(transaction);
        return true;
    }
}
