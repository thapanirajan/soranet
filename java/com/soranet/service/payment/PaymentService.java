package com.soranet.service.payment;

import com.soranet.config.DbConfig;
import com.soranet.model.PaymentModel;
import com.soranet.util.queries.PaymentModelQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {

    public boolean createPayment(PaymentModel payment) throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(PaymentModelQueries.INSERT_PAYMENT)) {
            stmt.setInt(1, payment.getSubscriptionId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setString(4, payment.getPaymentMethod());
            return stmt.executeUpdate() > 0;
        }
    }

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

    public boolean deletePaymentById(int paymentId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(PaymentModelQueries.DELETE_PAYMENT)) {
            pstmt.setInt(1, paymentId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}