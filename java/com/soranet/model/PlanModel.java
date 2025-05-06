package com.soranet.model;


import java.time.LocalDateTime;
import java.util.List;

public class PlanModel {
	private int planId;
	private String planName;
	private String speed;
	private double price;
	private String planDuration;
	private String planDescription;
	private String type; // Residential, Business
	private boolean popular;
	private List<String> features;
	private LocalDateTime createdAt;

	public PlanModel(int planId, String planName, String speed, double price, String planDuration,
			String planDescription, String type, boolean popular, List<String> features, LocalDateTime createdAt) {
		this.planId = planId;
		this.planName = planName;
		this.speed = speed;
		this.price = price;
		this.planDuration = planDuration;
		this.planDescription = planDescription;
		this.type = type;
		this.popular = popular;
		this.features = features;
		this.createdAt = createdAt;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPlanDuration() {
		return planDuration;
	}

	public void setPlanDuration(String planDuration) {
		this.planDuration = planDuration;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPopular() {
		return popular;
	}

	public void setPopular(boolean popular) {
		this.popular = popular;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
