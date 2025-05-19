package com.soranet.util.queries;

public class PlanModelQueries {

	public static final String INSERT_PLAN = "INSERT INTO Plan (PlanName, Speed, Price, PlanDuration, PlanDescription, Type, Popular, Features) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String SELECT_PLAN_BY_ID = "SELECT * FROM Plan WHERE PlanId = ?";

	public static final String SELECT_PLAN_BY_TYPE = "SELECT * FROM Plan WHERE type = ?";

	public static final String SELECT_ALL_PLANS = "SELECT * FROM Plan ORDER BY CreatedAt DESC";

	public static final String UPDATE_PLAN = "UPDATE Plan SET PlanName = ?, Speed = ?, Price = ?, PlanDuration = ?, PlanDescription = ?, Type = ?, Popular = ?, Features = ? "
			+ "WHERE PlanId = ?";

	public static final String DELETE_PLAN = "DELETE FROM Plan WHERE PlanId = ?";

	public static final String COUNT_SUBS_BY_PLANID = "SELECT COUNT(*) FROM Subscription WHERE planId = ?";

	public static final String COUNT_PLAN_BY_PLANID = "SELECT COUNT(*) FROM Plan WHERE PlanId = ?";

	public static final String COUNT_EXISTING_PLAN_NAME_EXCLUDING_ID = "SELECT COUNT(*) FROM Plan WHERE PlanName = ? AND PlanId != ?";

	public static final String GET_PLAN_CREATED_AT_BY_ID = "SELECT CreatedAt FROM Plan WHERE PlanId = ?";

	public static final String COUNT_PLAN_BY_NAME = "SELECT COUNT(*) FROM Plan WHERE PlanName = ?";

	public static final String SEARCH_PLANS_BY_NAME_OR_SPEED = "SELECT * FROM Plan WHERE LOWER(PlanName) LIKE ? OR LOWER(Speed) LIKE ?";
}
