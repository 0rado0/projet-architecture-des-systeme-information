package com.t1.cardio.card.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.t1.cardio.card.controller.CardService;
import com.t1.cardio.card.model.Card;



@RestController
//@RequestMapping("/card")
public class CardRestController {

    @Autowired
    private CardService cardService;


    @RequestMapping(method=RequestMethod.GET, value="/card/{id}")
    public Card getCardByID(@PathVariable String id) {
        return cardService.getCardByID(Integer.valueOf(id));
    }

    @RequestMapping(method=RequestMethod.GET, value="/cards/user/{userId}")
    public List<Card> getCardByUserID(@PathVariable String userId) {
        return cardService.getCardByUserID(Integer.valueOf(userId));
    }

    @RequestMapping(method=RequestMethod.PUT, value="/card/{id}")
    public Card putCardByID(@PathVariable String id, @RequestBody Card card) {
        return cardService.setCardUserID(Integer.valueOf(id), card);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/card/{id}")
    public void delCard(@PathVariable String id) {
        cardService.delCard(Integer.valueOf(id));
    }

    @RequestMapping(method=RequestMethod.POST, value="/card")
    public void postCard() {
        cardService.generateCard();
    }

    @RequestMapping(method=RequestMethod.GET, value="/cards")
    public List<Card> getCard() {
        return cardService.getCard();
    }

    @RequestMapping(method=RequestMethod.GET, value="/cards/shop")
    public List<Card> getCardOnMarket() {
        return cardService.getCardOnMarket();
    }

    @RequestMapping(method=RequestMethod.GET, value="/cards/shop/{userId}")
    public List<Card> getCardOnMarketByUserId(@PathVariable String userId) {
        return cardService.getCardOnMarketByUserId(Integer.valueOf(userId));
    }

    @RequestMapping(method=RequestMethod.POST, value="/card/shop/{cardId}/{onMarket}")
    public void setCardOnMarket(@PathVariable String cardId, @PathVariable String onMarket) {
        cardService.setCardOnMarket(Integer.valueOf(cardId), Boolean.valueOf(onMarket));
    }

    @RequestMapping(method=RequestMethod.POST, value="/card/{cardId}/{userId}")
    public void updateCardUserID(@PathVariable String cardId, @PathVariable String userId) {
        cardService.updateCardUserID(Integer.valueOf(cardId), Integer.valueOf(userId));
    }

    @RequestMapping(method=RequestMethod.POST, value="/card/user/{userId}")
    public void giveCardToUser(@PathVariable String userId) {
        System.out.println("CardRestController: giveCardToUser called with userId: " + userId);
        cardService.giveCardToUser(Integer.valueOf(userId));
        System.out.println("CardRestController: giveCardToUser completed");
    }
}