package com.t1.cardio.shop.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.t1.cardio.shop.controller.ShopService;
import com.t1.cardio.shop.model.Card;
import com.t1.cardio.shop.model.ShopOrder;
import com.t1.cardio.shop.model.ShopTransaction;



@RestController
public class ShopRestCrt {
    
    @Autowired
    private ShopService shopService;

    @GetMapping("/shop/transactions")
    public List<ShopTransaction> getAllTransactions() {
        return shopService.getAllTransactions();
    }

    @GetMapping("/shop/cards")
    public List<Card> getCardsOnMarket()  {
        return shopService.getCardsOnMarket();
    }

    @GetMapping("/shop/cards/user/{userId}")
    public List<Card> getCardsOnMarketByUserId(@PathVariable int userId) {
        return shopService.getCardsOnMarketByUserId(userId);
    }

    @PostMapping("/shop/sell")
    public ResponseEntity<String> sellCard(@RequestBody ShopOrder shopOrder) {
        boolean result = shopService.sellCard(shopOrder);
        if (result) {
            return ResponseEntity.ok("Card sold successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to sell card");
        }
    }

    @PostMapping("/shop/buy")
    public ResponseEntity<String> buyCard(@RequestBody ShopOrder shopOrder) {
        boolean result = shopService.buyCard(shopOrder);
        if (result) {
            return ResponseEntity.ok("Card bought successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to buy card");
        }
    }
}
