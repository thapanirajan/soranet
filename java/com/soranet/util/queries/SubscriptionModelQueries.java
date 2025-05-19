package com.soranet.util.queries;

public class SubscriptionModelQueries {

	public static final String GET_ALL_SUBSCRIPTIONS = "SELECT * FROM Subscription";

	public static final String UPDATE_SUBSCRIPTION_BY_ID = "UPDATE Subscription SET userId = ?, planId = ?, startDate = ?, endDate = ? WHERE subscriptionId = ?";

	public static final String INSERT_SUBSCRIPTION = "INSERT INTO Subscription (UserId, PlanId, StartDate, EndDate, CreatedAt) VALUES (?, ?, ?, ?, ?)";

	public static final String CHECK_USER_EXISTS = "SELECT COUNT(*) FROM Users WHERE UserId = ?";

	public static final String CHECK_PLAN_EXISTS_AND_GET_PRICE = "SELECT Price FROM Plan WHERE PlanId = ?";

	public static final String CHECK_USER_EXISTS_BOOL = "SELECT COUNT(*) FROM Users WHERE UserId = ?";

	public static final String CHECK_PLAN_EXISTS_BOOL = "SELECT COUNT(*) FROM Plan WHERE PlanId = ?";

	public static final String GET_SUBSCRIPTIONS_BY_USER_OR_PLAN_ID = "SELECT * FROM Subscription WHERE UserId = ? OR PlanId = ?";
}
	