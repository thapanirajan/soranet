package com.soranet.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.soranet.model.InternetPlanModel;
import com.soranet.config.DbConfig;

public class CustomerPageService {
	public List<InternetPlanModel> getPlansByType(String type) {
		List<InternetPlanModel> plans = new ArrayList<>();
		String sql = "SELECT * FROM  Plan WHERE type = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, type);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String featuresStr = rs.getString("features");

					// separate features by comma
					List<String> features = featuresStr != null ? Arrays.asList(featuresStr.split(","))
							: new ArrayList<>();

					InternetPlanModel plan = new InternetPlanModel(rs.getInt("PlanId"), rs.getString("PlanName"),
							rs.getString("Speed"), rs.getDouble("Price"), rs.getString("PlanDuration"),
							rs.getString("PlanDescription"), rs.getString("Type"), features, rs.getBoolean("Popular"));
					plans.add(plan);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return plans;
	}

	// Get plan by plan ID
	public InternetPlanModel getPlanById(int planId) throws Exception {
		String sql = "SELECT * FROM plan WHERE PlanId = ?";

		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, planId);
				ResultSet rs = ps.executeQuery();
				//InternetPlanModel(int planId, String planName, String speed, double price, String planDuration,
				//String planDescription, String type, List<String> features, boolean popular) {
				if(rs.next()) {
					return new InternetPlanModel(
							rs.getInt("planId"),
							rs.getString("planName"),
							rs.getString("speed"),
							rs.getDouble("price"),
							rs.getString("planDuration"),
							rs.getString("planDescription"),
							rs.getString("type"),
							Arrays.asList(rs.getString("features").split("\\s*,\\s*")),
							rs.getBoolean("popular")
					); 
				} else {
					throw  new Exception("Plan not found");
				}
		} catch (SQLException | ClassNotFoundException e) {	
			throw new Exception("Error retrieving user: " + e.getMessage(), e);
		}
	}
}
