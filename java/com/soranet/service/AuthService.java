package com.soranet.service;

import com.soranet.config.DbConfig;
import com.soranet.model.UserModel;
import com.soranet.util.PasswordUtil;
import com.soranet.util.queries.UserModelQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;

public class AuthService {

    /**
     * Registers a new user in the database.
     */
    public static void registerUser(UserModel user) throws Exception {
        try (Connection conn = DbConfig.getDbConnection()) {
            conn.setAutoCommit(false); // Start transaction
            try {
                // Check if username already exists
                try (PreparedStatement checkStmt = conn.prepareStatement(UserModelQueries.COUNT_USER_BY_USERNAME)) {
                    checkStmt.setString(1, user.getUsername());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new Exception("Username '" + user.getUsername() + "' is already taken.");
                    }
                }

                // Check if email already exists
                try (PreparedStatement checkStmt = conn.prepareStatement(UserModelQueries.COUNT_USER_BY_EMAIL)) {
                    checkStmt.setString(1, user.getEmail());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new Exception("Email '" + user.getEmail() + "' is already registered.");
                    }
                }

                // Insert user
                try (PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, user.getUsername());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getRole());
                    pstmt.setString(4, user.getFirstName());
                    pstmt.setString(5, user.getLastName());
                    pstmt.setString(6, user.getEmail());
                    pstmt.setString(7, user.getPhoneNumber());
                    pstmt.setString(8, user.getAddress());
                    pstmt.setString(9, user.getCity());
                    if (user.getProfilePicture() != null) {
                        pstmt.setString(10, user.getProfilePicture());
                    } else {
                        pstmt.setNull(10, java.sql.Types.VARCHAR);
                    }
                    pstmt.executeUpdate();

                    // Retrieve generated UserId
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        user.setUserId(rs.getInt(1));
                    } else {
                        throw new SQLException("Failed to retrieve generated UserId.");
                    }
                }

                // Retrieve CreatedAt from the inserted user
                String selectSql = "SELECT CreatedAt FROM Users WHERE UserId = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                    pstmt.setInt(1, user.getUserId());
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        Timestamp createdAt = rs.getTimestamp("CreatedAt");
                        if (createdAt != null) {
                            user.setCreatedAt(createdAt.toLocalDateTime());
                        }
                    }
                }

                conn.commit(); // Commit transaction
            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                throw new Exception("Failed to register user: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true); // Restore auto-commit
            }
        } catch (ClassNotFoundException e) {
            throw new Exception("Database driver not found: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a user by their username.
     */
    public static UserModel getUserByUsername(String username) throws Exception {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
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
                    rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime() : null
                );
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error retrieving user: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a user's profile details.
     */
    public static void updateUser(String username, String firstName, String lastName, String phoneNumber,
                                 String address, String city, String profilePicture) throws Exception {
        String sql = "UPDATE Users SET FirstName = ?, LastName = ?, PhoneNumber = ?, Address = ?, City = ?, ProfilePicture = ?, UpdatedAt = CURRENT_TIMESTAMP WHERE Username = ?";
        try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, address);
            pstmt.setString(5, city);
            pstmt.setString(6, profilePicture);
            pstmt.setString(7, username);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("User with username '" + username + "' not found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error updating user: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a user by login ID (username or email).
     */
    public static UserModel getUserByLoginId(String loginId) throws Exception {
        String sql = "SELECT * FROM Users WHERE Username = ? OR Email = ?";
        try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginId);
            pstmt.setString(2, loginId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
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
                    rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime() : null
                );
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error retrieving user: " + e.getMessage(), e);
        }
    }

    /**
     * Generates and stores a token for a user with expiry in days.
     */
    public static String generateAndStoreToken(int userId, int expiryDays) throws SQLException, ClassNotFoundException {
        String token = UUID.randomUUID().toString();
        String hashedToken = PasswordUtil.hashPassword(token);

        try (Connection conn = DbConfig.getDbConnection()) {
            String sql = "INSERT INTO AuthToken (UserId, Token, ExpiryDate) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL ? DAY))";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, hashedToken);
                pstmt.setInt(3, expiryDays);
                pstmt.executeUpdate();
            }
        }
        return token;
    }

    /**
     * Validates a token and returns the associated user.
     */
    public static UserModel validateToken(String token) throws SQLException, ClassNotFoundException {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        String hashedToken = PasswordUtil.hashPassword(token);
        try (Connection conn = DbConfig.getDbConnection()) {
            String sql = "SELECT u.* FROM AuthToken t JOIN Users u ON t.UserId = u.UserId "
                    + "WHERE t.Token = ? AND t.ExpiryDate > NOW()";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, hashedToken);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
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
                        rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime() : null
                    );
                }
            }
        }
        return null;
    }
}