package com.t1.cardio.card.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Card {

    public enum CardStatus {
        GENERATING,  // En cours de génération
        READY,       // Prête à être utilisée
        FAILED       // Échec de génération
    }

    private String name;
    private String description;
    private String imgUrl;
    private String smallImgUrl;

    private boolean isOnMarket = false;

    @Id
    @GeneratedValue
    private int id;

    private float energy;
    private float hp;
    private float defence;
    private float attack;
    private float price;
    private Integer userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardStatus status = CardStatus.GENERATING;

    @Column(name = "generation_task_id")
    private Integer generationTaskId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getDefence() {
        return defence;
    }

    public void setDefence(float defence) {
        this.defence = defence;
    }

    public float getAttack() {
        return attack;
    }

    public void setAttack(float attack) {
        this.attack = attack;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isOnMarket() {
        return isOnMarket;
    }

    public void setOnMarket(boolean isOnMarket) {
        this.isOnMarket = isOnMarket;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public Integer getGenerationTaskId() {
        return generationTaskId;
    }

    public void setGenerationTaskId(Integer generationTaskId) {
        this.generationTaskId = generationTaskId;
    }

    public Card(String name, String description, String imgUrl, String smallImgUrl, int id, float energy, float hp, float defence, float attack, float price, Integer userId) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.smallImgUrl = smallImgUrl;
        this.id = id;
        this.energy = energy;
        this.hp = hp;
        this.defence = defence;
        this.attack = attack;
        this.price = price;
        this.userId = userId;
    }

    public Card() {
    }
}