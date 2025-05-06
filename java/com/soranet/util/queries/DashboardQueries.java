package com.soranet.util.queries;

public class DashboardQueries {
	// User Statistics
	public static final String TOTAL_USERS = "SELECT COUNT(*) AS total_users FROM User";

	public static final String NEW_USERS = "SELECT COUNT(*) AS new_users FROM User WHERE CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";

	public static final String ACTIVE_USERS = "SELECT COUNT(DISTINCT s.UserId) AS active_users FROM Subscription s WHERE s.EndDate >= CURDATE()";

	// Subscription Overview
	public static final String TOTAL_SUBSCRIPTIONS = "SELECT COUNT(*) AS total_subscriptions FROM Subscription WHERE EndDate >= CURDATE()";

	public static final String NEW_SUBSCRIPTIONS = "SELECT COUNT(*) AS new_subscriptions FROM Subscription WHERE CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";

	public static final String SUBSCRIPTIONS_BY_TYPE = "SELECT p.Type, COUNT(s.SubscriptionId) AS subscription_count FROM Subscription s JOIN Plan p ON s.PlanId = p.PlanId WHERE s.EndDate >= CURDATE() GROUP BY p.Type";

	// Payment Summary
	public static final String TOTAL_REVENUE = "SELECT SUM(Amount) AS total_revenue FROM Payment";

	public static final String RECENT_REVENUE = "SELECT SUM(Amount) AS recent_revenue FROM Payment WHERE PaymentDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";

	public static final String PAYMENT_METHODS_BREAKDOWN = "SELECT PaymentMethod, COUNT(*) AS payment_count, SUM(Amount) AS total_amount FROM Payment GROUP BY PaymentMethod";

	// Plan Insights
	public static final String TOTAL_PLANS = "SELECT COUNT(*) AS total_plans FROM Plan";

	public static final String POPULAR_PLANS = "SELECT PlanId, PlanName, Type, Price FROM Plan WHERE Popular = TRUE";

	public static final String PLANS_BY_TYPE = "SELECT Type, COUNT(*) AS plan_count FROM Plan GROUP BY Type";

	// Recent Activity
	public static final String RECENT_USERS = "SELECT UserId, Username, Email, CreatedAt FROM User ORDER BY CreatedAt DESC LIMIT 5";

	public static final String RECENT_SUBSCRIPTIONS = "SELECT s.SubscriptionId, u.Username, p.PlanName, s.StartDate, s.EndDate "
			+ "FROM Subscription s JOIN User u ON s.UserId = u.UserId JOIN Plan p ON s.PlanId = p.PlanId ORDER BY s.CreatedAt DESC LIMIT 5";

	public static final String RECENT_PAYMENTS = "SELECT p.PaymentId, u.Username, p.Amount, p.PaymentDate, p.PaymentMethod FROM Payment p JOIN Subscription s ON p.SubscriptionId = s.SubscriptionId JOIN User u ON s.UserId = u.UserId ORDER BY p.PaymentDate DESC LIMIT 5";
}