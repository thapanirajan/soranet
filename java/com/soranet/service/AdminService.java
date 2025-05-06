package com.soranet.service;

import com.soranet.config.DbConfig;
import com.soranet.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminService {

	// User Management
	public List<UserModel> getAllUsers() throws SQLException, ClassNotFoundException {
		List<UserModel> users = new ArrayList<>();
		String sql = "SELECT * FROM Users";
		try (Connection conn = DbConfig.getDbConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				UserModel user = new UserModel(
						rs.getInt("userId"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("role"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("email"),
						rs.getString("phoneNumber"),
						rs.getString("address"),
						rs.getString("city"),
						rs.getString("profilePicture"),
						rs.getTimestamp("createdAt").toLocalDateTime(),
						rs.getTimestamp("updatedAt").toLocalDateTime()
				);
				users.add(user);
			}
		}
		return users;
	}

	public boolean updateUserRole(int userId, String newRole) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE Users SET role = ? WHERE userId = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, newRole);
			pstmt.setInt(2, userId);
			return pstmt.executeUpdate() > 0;
		}
	}

	// Plan Management
	public boolean createInternetPlan(PlanModel plan) throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO Plan (planName, speed, price, planDuration, planDescription, type, popular, features, createdAt) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, plan.getPlanName());
			pstmt.setString(2, plan.getSpeed());
			pstmt.setDouble(3, plan.getPrice());
			pstmt.setString(4, plan.getPlanDuration());
			pstmt.setString(5, plan.getPlanDescription());
			pstmt.setString(6, plan.getType());
			pstmt.setBoolean(7, plan.isPopular());
			pstmt.setString(8, String.join(",", plan.getFeatures()));
			pstmt.setTimestamp(9, Timestamp.valueOf(plan.getCreatedAt()));
			return pstmt.executeUpdate() > 0;
		}
	}

	public boolean updateInternetPlan(PlanModel plan) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE Plan SET planName = ?, speed = ?, price = ?, planDuration = ?, "
				+ "planDescription = ?, type = ?, popular = ?, features = ? WHERE planId = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, plan.getPlanName());
			pstmt.setString(2, plan.getSpeed());
			pstmt.setDouble(3, plan.getPrice());
			pstmt.setString(4, plan.getPlanDuration());
			pstmt.setString(5, plan.getPlanDescription());
			pstmt.setString(6, plan.getType());
			pstmt.setBoolean(7, plan.isPopular());
			pstmt.setString(8, String.join(",", plan.getFeatures()));
			pstmt.setInt(9, plan.getPlanId());
			return pstmt.executeUpdate() > 0;
		}
	}

	public List<PlanModel> getAllPlans() throws SQLException, ClassNotFoundException {
		List<PlanModel> plans = new ArrayList<>();
		String sql = "SELECT * FROM Plan";
		try (Connection conn = DbConfig.getDbConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				List<String> features = rs.getString("features") != null
						? Arrays.asList(rs.getString("features").split("\\s*,\\s*"))
						: new ArrayList<>();

				PlanModel plan = new PlanModel(
						rs.getInt("planId"),
						rs.getString("planName"),
						rs.getString("speed"),
						rs.getDouble("price"),
						rs.getString("planDuration"),
						rs.getString("planDescription"),
						rs.getString("type"),
						rs.getBoolean("popular"),
						features,
						rs.getTimestamp("createdAt").toLocalDateTime()
				);
				plans.add(plan);
			}
		}
		return plans;
	}

	// Subscription Management
	public List<SubscriptionModel> getAllSubscriptions() throws SQLException, ClassNotFoundException {
		List<SubscriptionModel> subscriptions = new ArrayList<>();
		String sql = "SELECT * FROM Subscription";
		try (Connection conn = DbConfig.getDbConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				SubscriptionModel sub = new SubscriptionModel(
						rs.getInt("subscriptionId"),
						rs.getInt("userId"),
						rs.getInt("planId"),
						rs.getDate("startDate").toLocalDate(),
						rs.getDate("endDate").toLocalDate(),
						rs.getTimestamp("createdAt").toLocalDateTime()
				);
				subscriptions.add(sub);
			}
		}
		return subscriptions;
	}

	public boolean updateSubscription(SubscriptionModel subscription) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE Subscription SET userId = ?, planId = ?, startDate = ?, endDate = ? WHERE subscriptionId = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, subscription.getUserId());
			pstmt.setInt(2, subscription.getPlanId());
			pstmt.setDate(3, Date.valueOf(subscription.getStartDate()));
			pstmt.setDate(4, Date.valueOf(subscription.getEndDate()));
			pstmt.setInt(5, subscription.getSubscriptionId());
			return pstmt.executeUpdate() > 0;
		}
	}

	// Payment Management
	public List<PaymentModel> getAllPayments() throws SQLException, ClassNotFoundException {
		List<PaymentModel> payments = new ArrayList<>();
		String sql = "SELECT * FROM Payment";
		try (Connection conn = DbConfig.getDbConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				PaymentModel payment = new PaymentModel(
						rs.getInt("paymentId"),
						rs.getInt("subscriptionId"),
						rs.getDouble("amount"),
						rs.getTimestamp("paymentDate").toLocalDateTime(),
						rs.getString("paymentMethod")
				);
				payments.add(payment);
			}
		}
		return payments;
	}
}
