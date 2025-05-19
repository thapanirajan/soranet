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

/**
 * Service class for handling payment-related operations such as creating,
 * retrieving, and deleting payment records.
 */
public class PaymentService {

	/**
	 * Inserts a new payment record into the database.
	 *
	 * @param payment The PaymentModel object containing payment details.
	 * @return true if the payment was inserted successfully, false otherwise.
	 * @throws SQLException           if a database access error occurs.
	 * @throws ClassNotFoundException if the JDBC driver class is not found.
	 */
	public boolean createPayment(PaymentModel payment) throws SQLException, ClassNotFoundException {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(PaymentModelQueries.INSERT_PAYMENT)) {

			// Set parameters for the INSERT query
			stmt.setInt(1, payment.getSubscriptionId());
			stmt.setDouble(2, payment.getAmount());
			stmt.setTimestamp(3, java.sql.Timestamp.valueOf(payment.getPaymentDate()));
			stmt.setString(4, payment.getPaymentMethod());

			// Execute the query and check if a row was inserted
			return stmt.executeUpdate() > 0;
		}
	}

	/**
	 * Retrieves all payment records from the database.
	 *
	 * @return A list of PaymentModel objects.
	 * @throws SQLException           if a database access error occurs.
	 * @throws ClassNotFoundException if the JDBC driver class is not found.
	 */
	public List<PaymentModel> getAllPayments() throws SQLException, ClassNotFoundException {
		List<PaymentModel> payments = new ArrayList<>();

		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(PaymentModelQueries.GET_ALL_PAYMENTS);
				ResultSet rs = pstmt.executeQuery()) {

			// Iterate through the result set and build payment model list
			while (rs.next()) {
				PaymentModel payment = new PaymentModel(rs.getInt("paymentId"), rs.getInt("subscriptionId"),
						rs.getDouble("amount"), rs.getTimestamp("paymentDate").toLocalDateTime(),
						rs.getString("paymentMethod"));
				payments.add(payment);
			}
		}

		return payments;
	}

	/**
	 * Retrieves all payment records associated with a specific subscription ID.
	 *
	 * @param subscriptionId The ID of the subscription.
	 * @return A list of PaymentModel objects linked to the given subscription.
	 * @throws SQLException           if a database access error occurs.
	 * @throws ClassNotFoundException if the JDBC driver class is not found.
	 */
	public List<PaymentModel> getPaymentsBySubscriptionId(int subscriptionId)
			throws SQLException, ClassNotFoundException {
		List<PaymentModel> payments = new ArrayList<>();

		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(PaymentModelQueries.GET_PAYMENTS_BY_SUBSCRIPTION_ID)) {

			// Set the subscription ID parameter for the query
			pstmt.setInt(1, subscriptionId);

			// Execute the query and process the results
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PaymentModel payment = new PaymentModel(rs.getInt("paymentId"), rs.getInt("subscriptionId"),
							rs.getDouble("amount"), rs.getTimestamp("paymentDate").toLocalDateTime(),
							rs.getString("paymentMethod"));
					payments.add(payment);
				}
			}
		}

		return payments;
	}

	/**
	 * Deletes a payment record based on the payment ID.
	 *
	 * @param paymentId The ID of the payment to delete.
	 * @return true if the payment was deleted successfully, false otherwise.
	 * @throws SQLException           if a database access error occurs.
	 * @throws ClassNotFoundException if the JDBC driver class is not found.
	 */
	public boolean deletePaymentById(int paymentId) throws SQLException, ClassNotFoundException {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(PaymentModelQueries.DELETE_PAYMENT)) {

			// Set the payment ID parameter for deletion
			pstmt.setInt(1, paymentId);

			// Execute the update and check affected rows
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		}
	}
}
