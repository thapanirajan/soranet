package com.soranet.util.queries;

public class PlanModelQueries {
	// PlanModel Queries
    public static final String INSERT_PLAN = 
        "INSERT INTO Plan (PlanName, Speed, Price, PlanDuration, PlanDescription, Type, Popular, Features) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    public static final String SELECT_PLAN_BY_ID = 
        "SELECT * FROM Plan WHERE PlanId = ?";
    
    public static final String SELECT_PLAN_BY_TYPE  = "SELECT * FROM Plan WHERE type = ?";
    
    public static final String SELECT_ALL_PLANS = 
        "SELECT * FROM Plan ORDER BY CreatedAt DESC";
    
    public static final String UPDATE_PLAN = 
        "UPDATE Plan SET PlanName = ?, Speed = ?, Price = ?, PlanDuration = ?, PlanDescription = ?, Type = ?, Popular = ?, Features = ? " +
        "WHERE PlanId = ?";
    
    public static final String DELETE_PLAN = 
        "DELETE FROM Plan WHERE PlanId = ?";
    
    public static final String COUNT_TOTAL_PLANS = 
        "SELECT COUNT(*) AS total_plans FROM Plan";
    
    public static final String SELECT_POPULAR_PLANS = 
        "SELECT PlanId, PlanName, Type, Price FROM Plan WHERE Popular = TRUE";
    
    public static final String COUNT_PLANS_BY_TYPE = 
        "SELECT Type, COUNT(*) AS plan_count FROM Plan GROUP BY Type";

}
