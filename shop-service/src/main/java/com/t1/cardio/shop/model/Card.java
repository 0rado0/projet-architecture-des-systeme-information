package com.t1.cardio.shop.model;

public class Card {

    private String name;
    private String description;
    private String imgUrl;
    private String smallImgUrl;

    private boolean isOnMarket = false;

    private int id;

    private float energy;
    private float hp;
    private float defence;
    private float attack;
    private float price;
    private Integer userId;

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

    public boolean isIsOnMarket() {
        return isOnMarket;
    }

    public void setIsOnMarket(boolean isOnMarket) {
        this.isOnMarket = isOnMarket;
    }
}