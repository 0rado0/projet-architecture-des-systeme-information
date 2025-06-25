package com.t1.cardio.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class AppUser { // AppUser sinon erreur SQL, car c'est un mot clé
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private double funds; // Argent disponible

    public AppUser(String username, String email, String password, double funds) {
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
