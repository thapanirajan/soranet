package com.soranet.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SubscriptionModel {
	private int subscriptionId;
	private int userId;
	private int planId;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDateTime createdAt;

	public SubscriptionModel(int subscriptionId, int userId, int planId, LocalDate startDate, LocalDate endDate,
			LocalDateTime createdAt) {
		super();
		this.subscriptionId = subscriptionId;
		this.userId = userId;
		this.planId = planId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdAt = createdAt;
	}

	// Getters and Setters
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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}