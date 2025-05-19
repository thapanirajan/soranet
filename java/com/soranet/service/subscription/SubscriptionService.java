package com.soranet.service.subscription;

import com.soranet.config.DbConfig;
import com.soranet.model.SubscriptionModel;
import com.soranet.util.queries.SubscriptionModelQueries;
import com.soranet.util.queries.PaymentModelQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionService {

    public List<SubscriptionModel> getAllSubscriptions() throws SQLException, ClassNotFoundException {
        List<SubscriptionModel> subscriptions = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(SubscriptionModelQueries.GET_ALL_SUBSCRIPTIONS);
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
    
    public List<SubscriptionModel> getSubscriptionsByUserOrPlanId(int searchId) throws SQLException, ClassNotFoundException {
        List<SubscriptionModel> subscriptions = new ArrayList<>();
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(SubscriptionModelQueries.GET_SUBSCRIPTIONS_BY_USER_OR_PLAN_ID)) {
            pstmt.setInt(1, searchId);
            pstmt.setInt(2, searchId);
            try (ResultSet rs = pstmt.executeQuery()) {
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
        }
        return subscriptions;
    }


    public boolean updateSubscription(SubscriptionModel subscription) throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(SubscriptionModelQueries.UPDATE_SUBSCRIPTION_BY_ID)) {
            pstmt.setInt(1, subscription.getUserId());
            pstmt.setInt(2, subscription.getPlanId());
            pstmt.setDate(3, java.sql.Date.valueOf(subscription.getStartDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(subscription.getEndDate()));
            pstmt.setInt(5, subscription.getSubscriptionId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public int createSubscription(SubscriptionModel subscription) throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(SubscriptionModelQueries.INSERT_SUBSCRIPTION,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, subscription.getUserId());
            stmt.setInt(2, subscription.getPlanId());
            stmt.setDate(3, java.sql.Date.valueOf(subscription.getStartDate()));
            stmt.setDate(4, java.sql.Date.valueOf(subscription.getEndDate()));
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(subscription.getCreatedAt()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Failed to create subscription.");
            }
        }
    }

    public boolean createSubscriptionWithPayment(SubscriptionModel subscription, String paymentMethod)
            throws ClassNotFoundException, SQLException {
        try (Connection conn = DbConfig.getDbConnection()) {
            conn.setAutoCommit(false);

            if (subscription == null || subscription.getUserId() <= 0 || subscription.getPlanId() <= 0
                    || subscription.getStartDate() == null || subscription.getEndDate() == null) {
                throw new IllegalArgumentException("Required subscription fields are missing");
            }
            if (paymentMethod == null || paymentMethod.isEmpty() || !paymentMethod.equals("credit")
                    && !paymentMethod.equals("paypal") && !paymentMethod.equals("bank")) {
                throw new IllegalArgumentException("Invalid payment method");
            }

            // Check if user exists
            try (PreparedStatement checkStmt = conn.prepareStatement(SubscriptionModelQueries.CHECK_USER_EXISTS)) {
                checkStmt.setInt(1, subscription.getUserId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new IllegalArgumentException(
                                "User with ID " + subscription.getUserId() + " does not exist");
                    }
                }
            }

            // Check if plan exists and get price
            double planPrice;
            try (PreparedStatement checkStmt = conn.prepareStatement(SubscriptionModelQueries.CHECK_PLAN_EXISTS_AND_GET_PRICE)) {
                checkStmt.setInt(1, subscription.getPlanId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        planPrice = rs.getDouble("Price");
                    } else {
                        throw new IllegalArgumentException(
                                "Plan with ID " + subscription.getPlanId() + " does not exist");
                    }
                }
            }

            // Insert subscription
            int subscriptionId;
            try (PreparedStatement pstmt = conn.prepareStatement(SubscriptionModelQueries.INSERT_SUBSCRIPTION,
                    Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, subscription.getUserId());
                pstmt.setInt(2, subscription.getPlanId());
                pstmt.setDate(3, java.sql.Date.valueOf(subscription.getStartDate()));
                pstmt.setDate(4, java.sql.Date.valueOf(subscription.getEndDate()));
                pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new SQLException("Subscription creation failed, no rows inserted.");
                }

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        subscriptionId = rs.getInt(1);
                        subscription.setSubscriptionId(subscriptionId);
                    } else {
                        throw new SQLException("Failed to retrieve generated SubscriptionId.");
                    }
                }
            }

            // Insert payment
            try (PreparedStatement pstmt = conn.prepareStatement(PaymentModelQueries.INSERT_PAYMENT)) {
                pstmt.setInt(1, subscriptionId);
                pstmt.setDouble(2, planPrice);
                pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setString(4, paymentMethod);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new SQLException("Payment creation failed, no rows inserted.");
                }
            }

            conn.commit();
            return true;
        }
    }

    public boolean userExists(int userId) throws ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(SubscriptionModelQueries.CHECK_USER_EXISTS_BOOL)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("SubscriptionService.userExists: SQL Error: " + e.getMessage());
            throw new RuntimeException("Error checking user existence: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean planExists(int planId) throws ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(SubscriptionModelQueries.CHECK_PLAN_EXISTS_BOOL)) {
            pstmt.setInt(1, planId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("SubscriptionService.planExists: SQL Error: " + e.getMessage());
            throw new RuntimeException("Error checking plan existence: " + e.getMessage(), e);
        }
        return false;
    }
}
