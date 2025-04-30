package com.soranet.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.soranet.config.DbConfig;

public class DashboardStatsService {
	// Total registered users
	public int getTotalUsers() throws SQLException, ClassNotFoundException {
		String sql = "SELECT COUNT(*) FROM users";
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	// New users this month
	public int getNewUsersThisMonth() throws SQLException, ClassNotFoundException {
		String sql = "SELECT COUNT(*) FROM users WHERE MONTH(created_at) = MONTH(CURRENT_DATE())";
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			return rs.next() ? rs.getInt(1) : 0;
		}
	}
	
	// Active subscriptions
    public int getActiveSubscriptions() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM subscriptions WHERE end_date > CURDATE()";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Subscriptions expiring in 7 days
    public int getExpiringSubscriptions() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM subscriptions WHERE end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
 // Total revenue
    public double getTotalRevenue() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(amount) FROM payments";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    // Monthly revenue
    public double getCurrentMonthRevenue() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(amount) FROM payments WHERE MONTH(payment_date) = MONTH(CURRENT_DATE())";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }
    // Get most popular plans
    public List<PlanPopularity> getPopularPlans() throws SQLException, ClassNotFoundException {
        List<PlanPopularity> plans = new ArrayList<>();
        String sql = "SELECT p.plan_name, COUNT(s.plan_id) AS subscribers " +
                     "FROM plans p LEFT JOIN subscriptions s ON p.plan_id = s.plan_id " +
                     "GROUP BY p.plan_id ORDER BY subscribers DESC LIMIT 5";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                plans.add(new PlanPopularity(
                    rs.getString("plan_name"),
                    rs.getInt("subscribers")
                ));
            }
        }
        return plans;
    }
    
    public static class PlanPopularity {
        private String planName;
        private int subscribers;

        public PlanPopularity(String planName, int subscribers) {
            this.planName = planName;
            this.subscribers = subscribers;
        }

        public String getPlanName() {
            return planName;
        }

        public int getSubscribers() {
            return subscribers;
        }
    }
    
    
    public List<Double> getMonthlyRevenueHistory() throws SQLException, ClassNotFoundException {
        List<Double> revenueHistory = new ArrayList<>();
        String sql = "SELECT MONTH(payment_date) AS month, SUM(amount) AS total " +
                     "FROM payments " +
                     "WHERE payment_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
                     "GROUP BY MONTH(payment_date) " +
                     "ORDER BY payment_date";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                revenueHistory.add(rs.getDouble("total"));
            }
        }
        return revenueHistory;
    }
}
