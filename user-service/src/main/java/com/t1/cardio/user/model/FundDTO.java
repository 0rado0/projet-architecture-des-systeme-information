package com.t1.cardio.user.model;

public class FundDTO {

    private Integer userId;
    private double funds;

    public FundDTO(Integer userId, double funds) {
        this.userId = userId;
        this.funds = funds;
    }

    public FundDTO() {
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }

    public double getFunds() {
        return funds;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "FundDTO{" +
                "userId=" + userId +
                ", funds=" + funds +
                '}';
    }
}
