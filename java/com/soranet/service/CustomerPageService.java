package com.soranet.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.soranet.model.PlanModel;
import com.soranet.util.queries.PlanModelQueries;
import com.soranet.config.DbConfig;

public class CustomerPageService {

    public static List<PlanModel> getPlansByType(String type) {
        List<PlanModel> plans = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(PlanModelQueries.SELECT_PLAN_BY_TYPE)) {

            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String featuresStr = rs.getString("features");
                    List<String> features = featuresStr != null
                            ? Arrays.asList(featuresStr.split("\\s*,\\s*"))
                            : new ArrayList<>();

                    Timestamp timestamp = rs.getTimestamp("createdAt");
                    LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;

                    PlanModel plan = new PlanModel(
                            rs.getInt("PlanId"),
                            rs.getString("PlanName"),
                            rs.getString("Speed"),
                            rs.getDouble("Price"),
                            rs.getString("PlanDuration"),
                            rs.getString("PlanDescription"),
                            rs.getString("Type"),
                            rs.getBoolean("Popular"),
                            features,
                            createdAt
                    );

                    plans.add(plan);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return plans;
    }

    public static PlanModel getPlanById(int planId) throws Exception {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(PlanModelQueries.SELECT_PLAN_BY_ID)) {
            ps.setInt(1, planId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                List<String> features = Arrays.asList(
                        rs.getString("features").split("\\s*,\\s*")
                );

                Timestamp timestamp = rs.getTimestamp("createdAt");
                LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;

                return new PlanModel(
                        rs.getInt("PlanId"),
                        rs.getString("PlanName"),
                        rs.getString("Speed"),
                        rs.getDouble("Price"),
                        rs.getString("PlanDuration"),
                        rs.getString("PlanDescription"),
                        rs.getString("Type"),
                        rs.getBoolean("Popular"),
                        features,
                        createdAt
                );
            } else {
                throw new Exception("Plan not found");
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error retrieving plan: " + e.getMessage(), e);
        }
    }
}