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
				UserModel user = new UserModel(rs.getInt("userId"), rs.getString("firstName"), rs.getString("lastName"),
						rs.getString("email"), rs.getString("phoneNumber"), rs.getString("username"),
						rs.getString("password"), rs.getString("role"), rs.getString("address"), rs.getString("city"),
						rs.getString("profilePicture"));
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
	public boolean createInternetPlan(InternetPlanModel plan) throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO Plan (planName, speed, price, planDuration, planDescription, type, features, popular) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, plan.getPlanName());
			pstmt.setString(2, plan.getSpeed());
			pstmt.setDouble(3, plan.getPrice());
			pstmt.setString(4, plan.getPlanDuration());
			pstmt.setString(5, plan.getPlanDescription());
			pstmt.setString(6, plan.getType());
			pstmt.setString(7, String.join(",", plan.getFeatures()));
			pstmt.setBoolean(8, plan.isPopular());

			return pstmt.executeUpdate() > 0;
		}
	}

	public boolean updateInternetPlan(InternetPlanModel plan) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE Plan SET planName = ?, speed = ?, price = ?, planDuration = ?, "
				+ "planDescription = ?, type = ?, features = ?, popular = ? WHERE planId = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, plan.getPlanName());
			pstmt.setString(2, plan.getSpeed());
			pstmt.setDouble(3, plan.getPrice());
			pstmt.setString(4, plan.getPlanDuration());
			pstmt.setString(5, plan.getPlanDescription());
			pstmt.setString(6, plan.getType());
			pstmt.setString(7, String.join(",", plan.getFeatures()));
			pstmt.setBoolean(8, plan.isPopular());
			pstmt.setInt(9, plan.getPlanId());

			return pstmt.executeUpdate() > 0;
		}
	}

	// Subscription Management
	public List<SubscriptionModel> getAllSubscriptions() throws SQLException, ClassNotFoundException {
		List<SubscriptionModel> subscriptions = new ArrayList<>();
		String sql = "SELECT * FROM Subscription";
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				SubscriptionModel sub = new SubscriptionModel(rs.getInt("subscriptionId"), rs.getInt("userId"),
						rs.getInt("planId"), rs.getString("startDate"), rs.getString("endDate"));
				subscriptions.add(sub);
			}
		}
		return subscriptions;
	}

	// Payment Management
	public List<PaymentModel> getAllPayments() throws SQLException, ClassNotFoundException {
		List<PaymentModel> payments = new ArrayList<>();
		String sql = "SELECT * FROM Payment";
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				PaymentModel payment = new PaymentModel(rs.getInt("paymentId"), rs.getInt("subscriptionId"),
						rs.getDouble("amount"), rs.getString("paymentDate"), rs.getString("paymentMethod"));
				payments.add(payment);
			}
		}
		return payments;
	}

	public List<InternetPlanModel> getAllPlans() throws SQLException, ClassNotFoundException {
		List<InternetPlanModel> plans = new ArrayList<>();
		String sql = "SELECT * FROM Plan";

		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				String featuresStr = rs.getString("features");
				List<String> features = featuresStr != null ? Arrays.asList(featuresStr.split("\\s*,\\s*"))
						: new ArrayList<>();

				InternetPlanModel plan = new InternetPlanModel(rs.getInt("planId"), rs.getString("planName"),
						rs.getString("speed"), rs.getDouble("price"), rs.getString("planDuration"),
						rs.getString("planDescription"), rs.getString("type"), features, rs.getBoolean("popular"));
				plans.add(plan);
			}
		}
		return plans;
	}

	public boolean updateSubscription(SubscriptionModel subscription) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE Subscription SET userId = ?, planId = ?, startDate = ?, endDate = ? WHERE subscriptionId = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, subscription.getUserId());
			pstmt.setInt(2, subscription.getPlanId());
			pstmt.setString(3, subscription.getStartDate());
			pstmt.setString(4, subscription.getEndDate());
			pstmt.setInt(5, subscription.getSubscriptionId());
			return pstmt.executeUpdate() > 0;
		}
	}
}