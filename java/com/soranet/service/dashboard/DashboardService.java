package com.soranet.service.dashboard;

import com.soranet.config.DbConfig;
import com.soranet.util.queries.DashboardQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for retrieving dashboard-related data. Provides methods to
 * fetch various metrics and lists for display on an admin dashboard.
 */
public class DashboardService {

	/**
	 * Retrieves comprehensive dashboard data, including user counts, subscriptions,
	 * revenue, and recent activity.
	 *
	 * @return a Map containing various dashboard metrics and lists
	 * @throws ClassNotFoundException if the database driver is not found
	 * @throws SQLException           if a database error occurs
	 */
	public Map<String, Object> getDashboardData() throws ClassNotFoundException, SQLException {
		// Initialize dashboard data map
		Map<String, Object> dashboardData = new HashMap<>();
		try {
			// Populate dashboard data with metrics and lists
			dashboardData.put("totalUsers", getTotalUsers());
			dashboardData.put("totalSubscriptions", getTotalSubscriptions());
			dashboardData.put("totalRevenue", getTotalRevenue());
			dashboardData.put("totalPlans", getTotalPlans());
			dashboardData.put("subscriptionsByPlanType", getSubscriptionsByPlanType());
			dashboardData.put("recentUsers", getRecentUsers());
			dashboardData.put("recentSubscriptions", getRecentSubscriptions());
			dashboardData.put("recentPayments", getRecentPayments());
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching dashboard data: " + e.getMessage(), e);
		}
		return dashboardData;
	}

	/**
	 * Retrieves the total number of users in the system.
	 *
	 * @return the total number of users
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private int getTotalUsers() throws SQLException, ClassNotFoundException {
		// Query total users
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(DashboardQueries.TOTAL_USERS);
				ResultSet rs = pstmt.executeQuery()) {
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	/**
	 * Retrieves the total number of subscriptions in the system.
	 *
	 * @return the total number of subscriptions
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private long getTotalSubscriptions() throws SQLException, ClassNotFoundException {
		// Query total subscriptions
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DashboardQueries.TOTAL_SUBSCRIPTIONS);
				ResultSet rs = stmt.executeQuery()) {
			return rs.next() ? rs.getLong("total_subscriptions") : 0;
		}
	}

	/**
	 * Retrieves the total revenue from all payments.
	 *
	 * @return the total revenue amount
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private double getTotalRevenue() throws SQLException, ClassNotFoundException {
		// Query total revenue
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DashboardQueries.TOTAL_REVENUE);
				ResultSet rs = stmt.executeQuery()) {
			return rs.next() ? rs.getDouble("total_revenue") : 0.0;
		}
	}

	/**
	 * Retrieves the total number of plans in the system.
	 *
	 * @return the total number of plans
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private long getTotalPlans() throws SQLException, ClassNotFoundException {
		// Query total plans
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DashboardQueries.TOTAL_PLANS);
				ResultSet rs = stmt.executeQuery()) {
			return rs.next() ? rs.getLong("total_plans") : 0;
		}
	}

	/**
	 * Retrieves the number of subscriptions grouped by plan type.
	 *
	 * @return a list of maps containing plan type and subscription count
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private List<Map<String, Object>> getSubscriptionsByPlanType() throws SQLException, ClassNotFoundException {
		List<Map<String, Object>> result = new ArrayList<>();
		// Query subscriptions by plan type
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DashboardQueries.SUBSCRIPTIONS_BY_PLAN_TYPE);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				// Construct map for each plan type
				Map<String, Object> row = new HashMap<>();
				row.put("type", rs.getString("Type"));
				row.put("subscription_count", rs.getInt("subscription_count"));
				result.add(row);
			}
		}
		return result;
	}

	/**
	 * Retrieves a list of recently registered users.
	 *
	 * @return a list of maps containing user details (ID, username, email, creation
	 *         date)
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private List<Map<String, Object>> getRecentUsers() throws SQLException, ClassNotFoundException {
		List<Map<String, Object>> result = new ArrayList<>();
		// Query recent users
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DashboardQueries.RECENT_USERS);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				// Construct map for each user
				Map<String, Object> row = new HashMap<>();
				row.put("userId", rs.getInt("UserId"));
				row.put("username", rs.getString("Username"));
				row.put("email", rs.getString("Email"));
				row.put("createdAt", rs.getTimestamp("CreatedAt"));
				result.add(row);
			}
		}
		return result;
	}

	/**
	 * Retrieves a list of recent subscriptions.
	 *
	 * @return a list of maps containing subscription details (ID, username, plan
	 *         name, start/end dates)
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private List<Map<String, Object>> getRecentSubscriptions() throws SQLException, ClassNotFoundException {
		List<Map<String, Object>> result = new ArrayList<>();
		// Query recent subscriptions
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DashboardQueries.RECENT_SUBSCRIPTIONS);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				// Construct map for each subscription
				Map<String, Object> row = new HashMap<>();
				row.put("subscriptionId", rs.getInt("SubscriptionId"));
				row.put("username", rs.getString("Username"));
				row.put("planName", rs.getString("PlanName"));
				row.put("startDate", rs.getDate("StartDate"));
				row.put("endDate", rs.getDate("EndDate"));
				result.add(row);
			}
		}
		return result;
	}

	/**
	 * Retrieves a list of recent payments.
	 *
	 * @return a list of maps containing payment details (ID, username, amount,
	 *         date, method)
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	private List<Map<String, Object>> getRecentPayments() throws SQLException, ClassNotFoundException {
		List<Map<String, Object>> result = new ArrayList<>();
		// Query recent payments
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DashboardQueries.RECENT_PAYMENTS);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				// Construct map for each payment
				Map<String, Object> row = new HashMap<>();
				row.put("paymentId", rs.getInt("PaymentId"));
				row.put("username", rs.getString("Username"));
				row.put("amount", rs.getDouble("Amount"));
				row.put("paymentDate", rs.getTimestamp("PaymentDate"));
				row.put("paymentMethod", rs.getString("PaymentMethod"));
				result.add(row);
			}
		}
		return result;
	}
}