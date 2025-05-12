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

public class DashboardService {

    public Map<String, Object> getDashboardData() throws ClassNotFoundException, SQLException {
        Map<String, Object> dashboardData = new HashMap<>();
        try {
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

    private int getTotalUsers() throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(DashboardQueries.TOTAL_USERS);
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private long getTotalSubscriptions() throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DashboardQueries.TOTAL_SUBSCRIPTIONS);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong("total_subscriptions") : 0;
        }
    }

    private double getTotalRevenue() throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DashboardQueries.TOTAL_REVENUE);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble("total_revenue") : 0.0;
        }
    }

    private long getTotalPlans() throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DashboardQueries.TOTAL_PLANS);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong("total_plans") : 0;
        }
    }

    private List<Map<String, Object>> getSubscriptionsByPlanType() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DashboardQueries.SUBSCRIPTIONS_BY_PLAN_TYPE);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("type", rs.getString("Type"));
                row.put("subscription_count", rs.getInt("subscription_count"));
                result.add(row);
            }
        }
        return result;
    }

    private List<Map<String, Object>> getRecentUsers() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DashboardQueries.RECENT_USERS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
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

    private List<Map<String, Object>> getRecentSubscriptions() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DashboardQueries.RECENT_SUBSCRIPTIONS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
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

    private List<Map<String, Object>> getRecentPayments() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(DashboardQueries.RECENT_PAYMENTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
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