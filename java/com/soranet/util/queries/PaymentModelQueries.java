package com.soranet.util.queries;

public class PaymentModelQueries {
	public static final String INSERT_PAYMENT = 
	        "INSERT INTO Payment (SubscriptionId, Amount, PaymentDate, PaymentMethod) VALUES (?, ?, ?, ?)";
	    
	    public static final String SELECT_PAYMENT_BY_ID = 
	        "SELECT * FROM Payment WHERE PaymentId = ?";
	    
	    public static final String SELECT_PAYMENTS_BY_SUBSCRIPTION = 
	        "SELECT p.*, u.Username FROM Payment p JOIN Subscription s ON p.SubscriptionId = s.SubscriptionId " +
	        "JOIN User u ON s.UserId = u.UserId WHERE p.SubscriptionId = ?";
	    
	    public static final String SELECT_ALL_PAYMENTS = 
	        "SELECT p.*, u.Username, s.PlanId FROM Payment p JOIN Subscription s ON p.SubscriptionId = s.SubscriptionId " +
	        "JOIN User u ON s.UserId = u.UserId ORDER BY p.PaymentDate DESC";
	    
	    public static final String UPDATE_PAYMENT = 
	        "UPDATE Payment SET Amount = ?, PaymentDate = ?, PaymentMethod = ? WHERE PaymentId = ?";
	    
	    public static final String DELETE_PAYMENT = 
	        "DELETE FROM Payment WHERE PaymentId = ?";
	    
	    public static final String TOTAL_REVENUE = 
	        "SELECT SUM(Amount) AS total_revenue FROM Payment";
	    
	    public static final String RECENT_REVENUE = 
	        "SELECT SUM(Amount) AS recent_revenue FROM Payment WHERE PaymentDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
	    
	    public static final String PAYMENT_METHODS_BREAKDOWN = 
	        "SELECT PaymentMethod, COUNT(*) AS payment_count, SUM(Amount) AS total_amount FROM Payment GROUP BY PaymentMethod";
	    
	    public static final String SELECT_RECENT_PAYMENTS = 
	        "SELECT p.PaymentId, u.Username, p.Amount, p.PaymentDate, p.PaymentMethod " +
	        "FROM Payment p JOIN Subscription s ON p.SubscriptionId = s.SubscriptionId JOIN User u ON s.UserId = u.UserId " +
	        "ORDER BY p.PaymentDate DESC LIMIT 5";
}
