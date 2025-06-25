package com.t1.cardio.user.model;

public class AuthDTO {

    private String username;
    private String password;

    public AuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDTO() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "AuthDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
