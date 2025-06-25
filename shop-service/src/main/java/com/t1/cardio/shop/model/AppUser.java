package com.t1.cardio.shop.model;

public class AppUser { // AppUser sinon erreur SQL, car c'est un mot clé
    private Integer id;
    private String username;
    private String email;
    private String password;
    private double funds; // Argent disponible

    public AppUser(Integer id, String username, String email, String password, double funds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.funds = funds;
    }

    public AppUser() {
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getFunds() {
        return funds;
    }

    public Integer getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }
}
