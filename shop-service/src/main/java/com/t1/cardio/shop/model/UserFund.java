package com.t1.cardio.shop.model;

public class UserFund {
    private Integer userId;
    private double funds;

    
	public UserFund(Integer userId, double funds) {
		this.userId = userId;
		this.funds = funds;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public double getFunds() {
		return funds;
	}
	public void setFunds(double funds) {
		this.funds = funds;
	}

    
}
