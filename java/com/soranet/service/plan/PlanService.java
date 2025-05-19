package com.soranet.service.plan;

import com.soranet.config.DbConfig;
import com.soranet.model.PlanModel;
import com.soranet.util.ValidationUtil;
import com.soranet.util.queries.PlanModelQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanService {

	public List<PlanModel> getAllPlans() throws ClassNotFoundException, SQLException {
		List<PlanModel> plans = new ArrayList<>();
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(PlanModelQueries.SELECT_ALL_PLANS);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				PlanModel plan = new PlanModel(rs.getInt("PlanId"), rs.getString("PlanName"), rs.getString("Speed"),
						rs.getDouble("Price"), rs.getString("PlanDuration"), rs.getString("PlanDescription"),
						rs.getString("Type"), rs.getBoolean("Popular"),
						rs.getString("Features") != null ? Arrays.asList(rs.getString("Features").split("\\s*,\\s*"))
								: List.of(),
						rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null);
				plans.add(plan);
			}
		}
		return plans;
	}

	public List<PlanModel> searchPlansByNameOrSpeed(String query) throws ClassNotFoundException, SQLException {
		List<PlanModel> plans = new ArrayList<>();
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(PlanModelQueries.SEARCH_PLANS_BY_NAME_OR_SPEED)) {
			String searchPattern = "%" + query.toLowerCase() + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PlanModel plan = new PlanModel(rs.getInt("PlanId"), rs.getString("PlanName"), rs.getString("Speed"),
							rs.getDouble("Price"), rs.getString("PlanDuration"), rs.getString("PlanDescription"),
							rs.getString("Type"), rs.getBoolean("Popular"),
							rs.getString("Features") != null
									? Arrays.asList(rs.getString("Features").split("\\s*,\\s*"))
									: List.of(),
							rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime()
									: null);
					plans.add(plan);
				}
			}
		}
		return plans;
	}

	public List<PlanModel> getPlansByType(String type) throws SQLException, ClassNotFoundException {
		List<PlanModel> plans = new ArrayList<>();
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(PlanModelQueries.SELECT_PLAN_BY_TYPE)) {
			stmt.setString(1, type);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String featuresStr = rs.getString("features");
					List<String> features = featuresStr != null ? Arrays.asList(featuresStr.split("\\s*,\\s*"))
							: new ArrayList<>();
					Timestamp timestamp = rs.getTimestamp("createdAt");
					LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;

					PlanModel plan = new PlanModel(rs.getInt("PlanId"), rs.getString("PlanName"), rs.getString("Speed"),
							rs.getDouble("Price"), rs.getString("PlanDuration"), rs.getString("PlanDescription"),
							rs.getString("Type"), rs.getBoolean("Popular"), features, createdAt);
					plans.add(plan);
				}
			}
		}
		return plans;
	}

	public PlanModel getPlanById(int planId) throws Exception {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement ps = conn.prepareStatement(PlanModelQueries.SELECT_PLAN_BY_ID)) {
			ps.setInt(1, planId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					List<String> features = Arrays.asList(rs.getString("features").split("\\s*,\\s*"));
					Timestamp timestamp = rs.getTimestamp("createdAt");
					LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;

					return new PlanModel(rs.getInt("PlanId"), rs.getString("PlanName"), rs.getString("Speed"),
							rs.getDouble("Price"), rs.getString("PlanDuration"), rs.getString("PlanDescription"),
							rs.getString("Type"), rs.getBoolean("Popular"), features, createdAt);
				}
				throw new Exception("Plan not found");
			}
		}
	}

	public boolean createPlan(PlanModel plan) throws ClassNotFoundException, SQLException {
		try (Connection conn = DbConfig.getDbConnection()) {
			conn.setAutoCommit(false);

			if (ValidationUtil.isNullOrEmpty(plan.getPlanName()) || ValidationUtil.isNullOrEmpty(plan.getSpeed())
					|| ValidationUtil.isNullOrEmpty(plan.getPlanDuration())
					|| ValidationUtil.isNullOrEmpty(plan.getType()) || plan.getPrice() < 0) {
				throw new IllegalArgumentException("Required plan fields are missing or invalid");
			}

			try (PreparedStatement checkStmt = conn.prepareStatement(PlanModelQueries.COUNT_PLAN_BY_NAME)) {
				checkStmt.setString(1, plan.getPlanName());
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						return false; // Plan name exists
					}
				}
			}

			try (PreparedStatement pstmt = conn.prepareStatement(PlanModelQueries.INSERT_PLAN,
					Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, plan.getPlanName());
				pstmt.setString(2, plan.getSpeed());
				pstmt.setDouble(3, plan.getPrice());
				pstmt.setString(4, plan.getPlanDuration());
				pstmt.setString(5, plan.getPlanDescription());
				pstmt.setString(6, plan.getType());
				pstmt.setBoolean(7, plan.isPopular());
				pstmt.setString(8, plan.getFeatures() != null ? String.join(",", plan.getFeatures()) : null);

				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted == 0) {
					throw new SQLException("Plan creation failed, no rows inserted.");
				}

				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						plan.setPlanId(rs.getInt(1));
					} else {
						throw new SQLException("Failed to retrieve generated PlanId.");
					}
				}
			}

			try (PreparedStatement pstmt = conn
					.prepareStatement(PlanModelQueries.COUNT_EXISTING_PLAN_NAME_EXCLUDING_ID)) {
				pstmt.setInt(1, plan.getPlanId());
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						Timestamp createdAt = rs.getTimestamp("CreatedAt");
						plan.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
					}
				}
			}

			conn.commit();
			return true;
		}
	}

	public boolean updatePlan(PlanModel plan) throws ClassNotFoundException, SQLException {
		try (Connection conn = DbConfig.getDbConnection()) {
			conn.setAutoCommit(false);

			if (ValidationUtil.isNullOrEmpty(plan.getPlanName()) || ValidationUtil.isNullOrEmpty(plan.getSpeed())
					|| ValidationUtil.isNullOrEmpty(plan.getPlanDuration())
					|| ValidationUtil.isNullOrEmpty(plan.getType()) || plan.getPrice() < 0) {
				throw new IllegalArgumentException("Required plan fields are missing or invalid");
			}

			try (PreparedStatement checkStmt = conn
					.prepareStatement(PlanModelQueries.COUNT_EXISTING_PLAN_NAME_EXCLUDING_ID)) {
				checkStmt.setString(1, plan.getPlanName());
				checkStmt.setInt(2, plan.getPlanId());
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						return false; // Plan name exists for another plan
					}
				}
			}

			try (PreparedStatement pstmt = conn.prepareStatement(PlanModelQueries.UPDATE_PLAN)) {
				pstmt.setString(1, plan.getPlanName());
				pstmt.setString(2, plan.getSpeed());
				pstmt.setDouble(3, plan.getPrice());
				pstmt.setString(4, plan.getPlanDuration());
				pstmt.setString(5, plan.getPlanDescription());
				pstmt.setString(6, plan.getType());
				pstmt.setBoolean(7, plan.isPopular());
				pstmt.setString(8, plan.getFeatures() != null ? String.join(",", plan.getFeatures()) : null);
				pstmt.setInt(9, plan.getPlanId());

				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected == 0) {
					return false;
				}
			}

			conn.commit();
			return true;
		}
	}

	public boolean deletePlan(int planId) throws ClassNotFoundException, SQLException {
		try (Connection conn = DbConfig.getDbConnection()) {
			conn.setAutoCommit(false);

			try (PreparedStatement checkStmt = conn.prepareStatement(PlanModelQueries.COUNT_PLAN_BY_PLANID)) {
				checkStmt.setInt(1, planId);
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) == 0) {
						return false; // Plan not found
					}
				}
			}

			try (PreparedStatement checkStmt = conn.prepareStatement(PlanModelQueries.COUNT_SUBS_BY_PLANID)) {
				checkStmt.setInt(1, planId);
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						return false; // Cannot delete plan with active subscriptions
					}
				}
			}

			try (PreparedStatement pstmt = conn.prepareStatement(PlanModelQueries.DELETE_PLAN)) {
				pstmt.setInt(1, planId);
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected == 0) {
					return false;
				}
			}

			conn.commit();
			return true;
		}
	}

	public boolean planExists(int planId) throws ClassNotFoundException, SQLException {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(PlanModelQueries.COUNT_PLAN_BY_PLANID)) {
			pstmt.setInt(1, planId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	public boolean deletePlanIfNoUser(int planId) throws SQLException, ClassNotFoundException {
		// Step 1: Check if the plan is associated with any users
		boolean isPlanUsed = checkPlanUsage(planId);

		if (isPlanUsed) {
			// If the plan is in use by users, don't delete
			return false; // Indicate failure to delete because plan is in use
		}

		// Step 2: If no users are associated with the plan, delete it
		return deletePlan(planId); // Proceed with deletion
	}

	// Method to check if the plan is associated with any user subscriptions
	private boolean checkPlanUsage(int planId) throws SQLException, ClassNotFoundException {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(PlanModelQueries.COUNT_SUBS_BY_PLANID)) {
			pstmt.setInt(1, planId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // Return true if the plan is in use
			}
		}
		return false;
	}

}