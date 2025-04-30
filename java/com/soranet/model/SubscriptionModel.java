package com.soranet.model;

public class SubscriptionModel {
	private int subscriptionId; // PK
	private int userId; // FK
	private int planId; // FK
	private String startDate;
	private String endDate;

	public SubscriptionModel(int subscriptionId, int userId, int planId, String startDate, String endDate) {
		super();
		this.subscriptionId = subscriptionId;
		this.userId = userId;
		this.planId = planId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
