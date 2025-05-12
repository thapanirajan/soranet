package com.soranet.util.queries;

public class SubscriptionModelQueries {
	public static final String INSERT_SUBSCRIPTION = 
	        "INSERT INTO Subscription (UserId, PlanId, StartDate, EndDate,createdAt ) VALUES (?, ?, ?, ?,?)";
	    
	    public static final String SELECT_SUBSCRIPTION_BY_ID = 
	        "SELECT * FROM Subscription WHERE SubscriptionId = ?";
	    
	    public static final String SELECT_SUBSCRIPTIONS_BY_USER = 
	        "SELECT s.*, p.PlanName, p.Type FROM Subscription s JOIN Plan p ON s.PlanId = p.PlanId WHERE s.UserId = ?";
	    
	    public static final String SELECT_ALL_SUBSCRIPTIONS = 
	        "SELECT s.*, u.Username, p.PlanName FROM Subscription s JOIN User u ON s.UserId = u.UserId JOIN Plan p ON s.PlanId = p.PlanId " +
	        "ORDER BY s.CreatedAt DESC";
	    
	    public static final String UPDATE_SUBSCRIPTION = 
	        "UPDATE Subscription SET PlanId = ?, StartDate = ?, EndDate = ? WHERE SubscriptionId = ?";
	    
	    public static final String DELETE_SUBSCRIPTION = 
	        "DELETE FROM Subscription WHERE SubscriptionId = ?";
	    
	    public static final String COUNT_TOTAL_SUBSCRIPTIONS = 
	        "SELECT COUNT(*) AS total_subscriptions FROM Subscription WHERE EndDate >= CURDATE()";
	    
	    public static final String COUNT_NEW_SUBSCRIPTIONS = 
	        "SELECT COUNT(*) AS new_subscriptions FROM Subscription WHERE CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
	    
	    public static final String COUNT_SUBSCRIPTIONS_BY_TYPE = 
	        "SELECT p.Type, COUNT(s.SubscriptionId) AS subscription_count FROM Subscription s " +
	        "JOIN Plan p ON s.PlanId = p.PlanId WHERE s.EndDate >= CURDATE() GROUP BY p.Type";
	    
	    public static final String SELECT_RECENT_SUBSCRIPTIONS = 
	        "SELECT s.SubscriptionId, u.Username, p.PlanName, s.StartDate, s.EndDate " +
	        "FROM Subscription s JOIN User u ON s.UserId = u.UserId JOIN Plan p ON s.PlanId = p.PlanId " +
	        "ORDER BY s.CreatedAt DESC LIMIT 5";
}
