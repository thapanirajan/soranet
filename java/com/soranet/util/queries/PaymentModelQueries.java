package com.soranet.util.queries;

//
public class PaymentModelQueries {
	public static final String INSERT_PAYMENT = "INSERT INTO Payment (SubscriptionId, Amount, PaymentDate, PaymentMethod) VALUES (?, ?, ?, ?)";

	public static final String DELETE_PAYMENT = "DELETE FROM Payment WHERE PaymentId = ?";

	public static final String GET_ALL_PAYMENTS = "SELECT * FROM Payment";

	public static final String GET_PAYMENTS_BY_SUBSCRIPTION_ID = "SELECT * FROM Payment WHERE SubscriptionId = ?";

}
