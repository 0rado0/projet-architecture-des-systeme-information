package com.t1.cardio.shop.model;

public class ShopOrder {

    private int userId;
    private int cardId;

    public ShopOrder(int userId, int cardId) {
        this.userId = userId;
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCardId() {
        return cardId;
    }
}
