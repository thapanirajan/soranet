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
		} catch (SQLException e) {
			// Handle exception
		}

		return dashboardData;
	}
}
