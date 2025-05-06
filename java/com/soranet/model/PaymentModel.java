package com.soranet.model;


import java.time.LocalDateTime;

public class PaymentModel {
	private int paymentId;
	private int subscriptionId;
	private double amount;
	private LocalDateTime paymentDate;
	private String paymentMethod; // credit, paypal, bank

	
	public PaymentModel(int paymentId, int subscriptionId, double amount, LocalDateTime paymentDate,
			String paymentMethod) {
		super();
		this.paymentId = paymentId;
		this.subscriptionId = subscriptionId;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.paymentMethod = paymentMethod;
	}

	// Getters and Setters
	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}


	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}