package com.soranet.util.queries;

public class DashboardQueries {

	public static final String TOTAL_USERS = "SELECT COUNT(*) AS total_users FROM Users";

	public static final String TOTAL_SUBSCRIPTIONS = "SELECT COUNT(*) AS total_subscriptions FROM Subscription WHERE EndDate >= CURDATE()";

	public static final String TOTAL_REVENUE = "SELECT SUM(Amount) AS total_revenue FROM Payment";

	public static final String TOTAL_PLANS = "SELECT COUNT(*) AS total_plans FROM Plan";

	public static final String SUBSCRIPTIONS_BY_PLAN_TYPE = "SELECT p.Type, COUNT(s.SubscriptionId) AS subscription_count "
			+ "FROM Subscription s JOIN Plan p ON s.PlanId = p.PlanId WHERE s.EndDate >= CURDATE() "
			+ "GROUP BY p.Type";

	public static final String RECENT_USERS = "SELECT UserId, Username, Email, CreatedAt FROM Users ORDER BY CreatedAt DESC LIMIT 5";

	public static final String RECENT_SUBSCRIPTIONS = "SELECT s.SubscriptionId, u.Username, p.PlanName, s.StartDate, s.EndDate "
			+ "FROM Subscription s JOIN Users u ON s.UserId = u.UserId JOIN Plan p ON s.PlanId = p.PlanId "
			+ "ORDER BY s.CreatedAt DESC LIMIT 5";

	public static final String RECENT_PAYMENTS = "SELECT p.PaymentId, u.Username, p.Amount, p.PaymentDate, p.PaymentMethod "
			+ "FROM Payment p JOIN Subscription s ON p.SubscriptionId = s.SubscriptionId "
			+ "JOIN Users u ON s.UserId = u.UserId ORDER BY p.PaymentDate DESC LIMIT 5";
}
