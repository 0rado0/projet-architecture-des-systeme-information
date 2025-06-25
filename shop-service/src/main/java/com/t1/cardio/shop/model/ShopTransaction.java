package com.t1.cardio.shop.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class ShopTransaction {

    @Id
    private int id;
    private int sellerId;
    private int buyerId;
    private int cardId;
    private double amount;
    private Action action;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timeSt;

    private static int idCounter = 0;

    public enum Action {
        BUY,
        SELL
    }

    public ShopTransaction() {
    }

    public ShopTransaction(int buyerId, int sellerId, int cardId, double amount, Action action, Timestamp timeSt) {
        this.id = getUniqueId();
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.cardId = cardId;
        this.amount = amount;
        this.action = action;
        this.timeSt = timeSt;
    }

    private int getUniqueId() {
        return idCounter++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyerId() {
        return this.buyerId;
    }
    public int getSellerId() {
        return sellerId;
    }
    public void setBuyerId(int userId) {
        this.buyerId = userId;
    }
    public void setSellerId(int userId) {
        this.sellerId = userId;
    }
    public int getCardId() {
        return cardId;
    }
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    public Timestamp getTimeSt() {
        return timeSt;
    }
    public void setTimeSt(Timestamp timeSt) {
        this.timeSt = timeSt;
    }


}