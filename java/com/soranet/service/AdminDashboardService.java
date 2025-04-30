package com.soranet.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardService {
	private final DashboardStatsService dashboardStats = new DashboardStatsService();

	public Map<String, Object> getDashboardData() throws ClassNotFoundException {
		Map<String, Object> dashboardData = new HashMap<>();

		try {
			// User Statistics
			dashboardData.put("totalUsers", dashboardStats.getTotalUsers());
			dashboardData.put("newUsers", dashboardStats.getNewUsersThisMonth());

			// Subscription Data
			dashboardData.put("activeSubs", dashboardStats.getActiveSubscriptions());
			dashboardData.put("expiringSubs", dashboardStats.getExpiringSubscriptions());

			// Revenue Data
			dashboardData.put("totalRevenue", dashboardStats.getTotalRevenue());
			dashboardData.put("monthlyRevenue", dashboardStats.getCurrentMonthRevenue());

			// Plan Popularity
			dashboardData.put("popularPlans", dashboardStats.getPopularPlans());

			dashboardData.put("revenueHistory", dashboardStats.getMonthlyRevenueHistory());

		} catch (SQLException e) {
			// Handle exception
		}

		return dashboardData;
	}
}
