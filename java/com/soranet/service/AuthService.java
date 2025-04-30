package com.soranet.service;

import com.soranet.config.DbConfig;
import com.soranet.model.UserModel;
import com.soranet.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthService {

	/**
	 * Registers a new user in the database.
	 *
	 * @param user the UserModel object containing user details
	 * @throws Exception if registration fails (e.g., duplicate username/email)
	 */
	public void registerUser(UserModel user) throws Exception {
		try (Connection conn = DbConfig.getDbConnection()) {
			// Check if username already exists
			String checkUsernameSql = "SELECT COUNT(*) FROM Users WHERE username = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkUsernameSql)) {
				checkStmt.setString(1, user.getUsername());
				ResultSet rs = checkStmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					throw new Exception("Username '" + user.getUsername() + "' is already taken.");
				}
			}

			// Check if email already exists
			String checkEmailSql = "SELECT COUNT(*) FROM Users WHERE email = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
				checkStmt.setString(1, user.getEmail());
				ResultSet rs = checkStmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					throw new Exception("Email '" + user.getEmail() + "' is already registered.");
				}
			}

			// If no duplicates, proceed with insertion
			String insertSql = "INSERT INTO Users (firstName, lastName, email, phoneNumber, username, password, role, address, city, profilePicture) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
				pstmt.setString(1, user.getFirstName());
				pstmt.setString(2, user.getLastName());
				pstmt.setString(3, user.getEmail());
				pstmt.setString(4, user.getPhoneNumber());
				pstmt.setString(5, user.getUsername());
				pstmt.setString(6, user.getPassword());
				pstmt.setString(7, user.getRole());
				pstmt.setString(8, user.getAddress());
				pstmt.setString(9, user.getCity());
				if (user.getProfilePicture() != null) {
					pstmt.setString(10, user.getProfilePicture());
				} else {
					pstmt.setNull(10, java.sql.Types.VARCHAR);
				}
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new Exception("Failed to register user: " + e.getMessage() + " (SQL State: " + e.getSQLState()
					+ ", Error Code: " + e.getErrorCode() + ")", e);
		} catch (ClassNotFoundException e) {
			throw new Exception("Database driver not found: " + e.getMessage(), e);
		}
	}

	/**
	 * Retrieves a user from the database by their username.
	 *
	 * @param username the username of the user to retrieve
	 * @return the UserModel object if found, or null if not found
	 * @throws Exception if a database error occurs
	 */
	public UserModel getUserByUsername(String username) throws Exception {
		String sql = "SELECT * FROM Users WHERE username = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new UserModel(rs.getInt("userId"), rs.getString("firstName"), rs.getString("lastName"),
						rs.getString("email"), rs.getString("phoneNumber"), rs.getString("username"),
						rs.getString("password"), rs.getString("role"), rs.getString("address"), rs.getString("city"),
						rs.getString("profilePicture"));
			}
			return null;
		} catch (SQLException | ClassNotFoundException e) {
			throw new Exception("Error retrieving user: " + e.getMessage(), e);
		}
	}

	/**
	 * Updates a user's profile details in the database.
	 *
	 * @param username    the username of the user to update
	 * @param firstName   the updated first name
	 * @param lastName    the updated last name
	 * @param phoneNumber the updated phone number
	 * @param address     the updated address
	 * @param city        the updated city
	 * @throws Exception if an error occurs during the update
	 */
	public void updateUser(String username, String firstName, String lastName, String phoneNumber, String address,
			String city, String profilePicture) throws Exception {
		String sql = "UPDATE Users SET firstName = ?, lastName = ?, phoneNumber = ?, address = ?, city = ?,profilePicture=? WHERE username = ?";
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
	 * Retrieves a user from the database by their login ID (username or email).
	 *
	 * @param loginId the username or email of the user to retrieve
	 * @return the UserModel object if found, or null if not found
	 * @throws Exception if a database error occurs
	 */
	public UserModel getUserByLoginId(String loginId) throws Exception {
		String sql = "SELECT * FROM Users WHERE username = ? OR email = ?";
		try (Connection conn = DbConfig.getDbConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, loginId);
			pstmt.setString(2, loginId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new UserModel(rs.getInt("userId"), rs.getString("firstName"), rs.getString("lastName"),
						rs.getString("email"), rs.getString("phoneNumber"), rs.getString("username"),
						rs.getString("password"), rs.getString("role"), rs.getString("address"), rs.getString("city"),
						rs.getString("profilePicture"));
			}
			return null;
		} catch (SQLException | ClassNotFoundException e) {
			throw new Exception("Error retrieving user: " + e.getMessage(), e);
		}
	}

	public static String generateAndStoreToken(int userId) throws SQLException, ClassNotFoundException {
		String token = UUID.randomUUID().toString();
		String hashedToken = PasswordUtil.hashPassword(token); // Store hashed token

		try (Connection conn = DbConfig.getDbConnection()) {
			String sql = "INSERT INTO auth_tokens (user_id, token, expiry_date) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 30 DAY))";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, userId);
				pstmt.setString(2, hashedToken);
				pstmt.executeUpdate();
			}
		}
		return token;
	}

	public static UserModel validateToken(String token) {
        try (Connection conn = DbConfig.getDbConnection()) {
            String hashedToken = PasswordUtil.hashPassword(token);
            String sql = "SELECT u.* FROM auth_tokens t " +
                        "JOIN users u ON t.user_id = u.userId " +
                        "WHERE t.token = ? AND t.expiry_date > NOW()";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, hashedToken);
                ResultSet rs = pstmt.executeQuery();
                //public UserModel(int userId, String firstName, String lastName, String email, String phoneNumber, String username,
    			//String password, String role, String address, String city, String profilePicture) {
                
                // if token matches and has not expired
                if (rs.next()) {
                    return new UserModel(
                        rs.getInt("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("profilePicture")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
