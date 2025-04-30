package com.soranet.model;

public class PaymentModel {
	private int paymentId; // PK
	// Foreign Key
	private int subscriptionId; // FK
	private double amount;
	private String paymentDate;
	private String paymentMethod;

	public PaymentModel(int paymentId, int subscriptionId, double amount, String paymentDate, String paymentMethod) {
		super();
		this.paymentId = paymentId;
		this.subscriptionId = subscriptionId;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.paymentMethod = paymentMethod;
	}

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

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
