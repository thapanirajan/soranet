package com.soranet.service.auth;

import com.soranet.config.DbConfig;
import com.soranet.model.UserModel;
import com.soranet.util.PasswordUtil;
import com.soranet.util.queries.UserModelQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Service class for handling user authentication operations.
 * Provides methods for retrieving users by login ID, generating and storing authentication tokens,
 * and validating tokens.
 */
public class AuthService {

    /**
     * Retrieves a user by their login ID (username or email).
     *
     * @param loginId the username or email to search for
     * @return the UserModel if found, or null if no user matches the login ID
     * @throws Exception if a database error occurs
     */
    public UserModel getUserByLoginId(String loginId) throws Exception {
        // Establish database connection and prepare query
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.GET_USER_BY_LOGINID)) {
            pstmt.setString(1, loginId);
            pstmt.setString(2, loginId);
            // Execute query and process results
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Construct and return UserModel from result set
                    return new UserModel(
                            rs.getInt("UserId"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Email"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("City"),
                            rs.getString("ProfilePicture"),
                            rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null,
                            rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime() : null);
                }
                return null;
            }
        }
    }

    /**
     * Generates a new authentication token for a user and stores it in the database.
     *
     * @param userId      the ID of the user to generate a token for
     * @param expiryDays  the number of days until the token expires
     * @return the generated token
     * @throws SQLException           if a database error occurs
     * @throws ClassNotFoundException if the database driver is not found
     */
    public String generateAndStoreToken(int userId, int expiryDays) throws SQLException, ClassNotFoundException {
        // Generate unique token
        String token = UUID.randomUUID().toString();
        // Hash token for secure storage
        String hashedToken = PasswordUtil.hashPassword(token);

        // Store hashed token in database
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.INSERT_AUTH_TOKEN)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, hashedToken);
            pstmt.setInt(3, expiryDays);
            pstmt.executeUpdate();
            return token;
        }
    }

    /**
     * Validates an authentication token and retrieves the associated user.
     *
     * @param token the authentication token to validate
     * @return the UserModel if the token is valid, or null if invalid or not found
     * @throws SQLException           if a database error occurs
     * @throws ClassNotFoundException if the database driver is not found
     */
    public UserModel validateToken(String token) throws SQLException, ClassNotFoundException {
        // Validate input token
        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        // Hash token for comparison
        String hashedToken = PasswordUtil.hashPassword(token);
        // Query database for valid token
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SELECT_USER_BY_VALID_TOKEN)) {
            pstmt.setString(1, hashedToken);
            // Process query results
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Construct and return UserModel from result set
                    return new UserModel(
                            rs.getInt("UserId"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Email"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("City"),
                            rs.getString("ProfilePicture"),
                            rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null,
                            rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime() : null);
                }
                return null;
            }
        }
    }
}