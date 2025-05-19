package com.soranet.service.auth;

import com.soranet.config.DbConfig;
import com.soranet.model.UserModel;
import com.soranet.util.queries.UserModelQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserService {

	public boolean registerUser(UserModel user) throws Exception {
		try (Connection conn = DbConfig.getDbConnection()) {
			conn.setAutoCommit(false);

			// Check if username exists
			try (PreparedStatement checkStmt = conn.prepareStatement(UserModelQueries.COUNT_USER_BY_USERNAME)) {
				checkStmt.setString(1, user.getUsername());
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						return false;
					}
				}
			}

			// Check if email exists
			try (PreparedStatement checkStmt = conn.prepareStatement(UserModelQueries.COUNT_USER_BY_EMAIL)) {
				checkStmt.setString(1, user.getEmail());
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						return false;
					}
				}
			}

			// Insert user
			try (PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.INSERT_USER,
					Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, user.getUsername());
				pstmt.setString(2, user.getPassword());
				pstmt.setString(3, user.getRole());
				pstmt.setString(4, user.getFirstName());
				pstmt.setString(5, user.getLastName());
				pstmt.setString(6, user.getEmail());
				pstmt.setString(7, user.getPhoneNumber());
				pstmt.setString(8, user.getAddress());
				pstmt.setString(9, user.getCity());
				pstmt.setString(10, user.getProfilePicture());

				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted == 0) {
					throw new SQLException("User registration failed, no rows inserted.");
				}

				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						user.setUserId(rs.getInt(1));
					} else {
						throw new SQLException("Failed to retrieve generated UserId.");
					}
				}
			}

			// Retrieve CreatedAt
			try (PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SELECT_CREATED_AT_BY_USERID)) {
				pstmt.setInt(1, user.getUserId());
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						Timestamp createdAt = rs.getTimestamp("CreatedAt");
						if (createdAt != null) {
							user.setCreatedAt(createdAt.toLocalDateTime());
						}
					}
				}
			}

			conn.commit();
			return true;
		} catch (SQLException e) {
			throw new Exception("Failed to register user: " + e.getMessage(), e);
		}
	}

	/**
	 * Retrieves a user by login ID (username or email).
	 */
	public static UserModel getUserByLoginId(String loginId) throws Exception {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.GET_USER_BY_LOGINID)) {
			pstmt.setString(1, loginId);
			pstmt.setString(2, loginId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new UserModel(rs.getInt("UserId"), rs.getString("Username"), rs.getString("Password"),
							rs.getString("Role"), rs.getString("FirstName"), rs.getString("LastName"),
							rs.getString("Email"), rs.getString("PhoneNumber"), rs.getString("Address"),
							rs.getString("City"), rs.getString("ProfilePicture"),
							rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime()
									: null,
							rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime()
									: null);
				}
				return null;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new Exception("Error retrieving user: " + e.getMessage(), e);
		}
	}

	public boolean updateUser(String username, String firstName, String lastName, String email, String phoneNumber,
			String address, String city, String profilePicture) throws Exception {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.UPDATE_USER_BY_USERNAME)) {
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, email);
			pstmt.setString(4, phoneNumber);
			pstmt.setString(5, address);
			pstmt.setString(6, city);
			pstmt.setString(7, profilePicture);
			pstmt.setString(8, username);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	public boolean deleteUser(int userId) throws ClassNotFoundException, SQLException {
		try (Connection conn = DbConfig.getDbConnection()) {
			conn.setAutoCommit(false);

			// Delete associated data
			deleteSubscriptionsByUserId(userId, conn);
			deleteAuthTokensById(userId);

			// Delete user
			try (PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.DELETE_USER_BY_USERID)) {
				pstmt.setInt(1, userId);
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected == 0) {
					return false;
				}
			}

			conn.commit();
			return true;
		}
	}

	public boolean updateUserRole(int userId, String newRole) throws SQLException, ClassNotFoundException {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.UPDATE_USER_ROLE_BY_USERID)) {
			pstmt.setString(1, newRole);
			pstmt.setInt(2, userId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	public List<UserModel> getAllUsers() throws ClassNotFoundException, SQLException {
		List<UserModel> users = new ArrayList<>();
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SELECT_ALL_USERS);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				UserModel user = new UserModel();
				user.setUserId(rs.getInt("UserId"));
				user.setUsername(rs.getString("Username"));
				user.setRole(rs.getString("Role"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setEmail(rs.getString("Email"));
				user.setPhoneNumber(rs.getString("PhoneNumber"));
				user.setAddress(rs.getString("Address"));
				user.setCity(rs.getString("City"));
				user.setProfilePicture(rs.getString("ProfilePicture"));
				Timestamp createdAt = rs.getTimestamp("CreatedAt");
				if (createdAt != null) {
					user.setCreatedAt(createdAt.toLocalDateTime());
				}
				users.add(user);
			}
		}
		return users;
	}

	public UserModel getUserByUsername(String username) throws Exception {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SELECT_USER_BY_USERNAME)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new UserModel(rs.getInt("UserId"), rs.getString("Username"), rs.getString("Password"),
							rs.getString("Role"), rs.getString("FirstName"), rs.getString("LastName"),
							rs.getString("Email"), rs.getString("PhoneNumber"), rs.getString("Address"),
							rs.getString("City"), rs.getString("ProfilePicture"),
							rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime()
									: null,
							rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime()
									: null);
				}
				return null;
			}
		}
	}

	public List<UserModel> searchUsersByNameOrEmail(String query) throws ClassNotFoundException, SQLException {
		List<UserModel> users = new ArrayList<>();
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SEARCH_USERS_BY_NAME_OR_EMAIL)) {
			String searchPattern = "%" + query.toLowerCase() + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					UserModel user = new UserModel();
					user.setUserId(rs.getInt("UserId"));
					user.setUsername(rs.getString("Username"));
					user.setRole(rs.getString("Role"));
					user.setFirstName(rs.getString("FirstName"));
					user.setLastName(rs.getString("LastName"));
					user.setEmail(rs.getString("Email"));
					user.setPhoneNumber(rs.getString("PhoneNumber"));
					user.setAddress(rs.getString("Address"));
					user.setCity(rs.getString("City"));
					user.setProfilePicture(rs.getString("ProfilePicture"));
					Timestamp createdAt = rs.getTimestamp("CreatedAt");
					if (createdAt != null) {
						user.setCreatedAt(createdAt.toLocalDateTime());
					}
					users.add(user);
				}
			}
		}
		return users;
	}

	private void deleteSubscriptionsByUserId(int userId, Connection conn) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.DELETE_SUBSCRIPTIONS_BY_USERID)) {
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		}
	}

	public void deleteAuthTokensById(int userId) throws SQLException, ClassNotFoundException {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.DELETE_AUTHTOKENS_BY_USERID)) {
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		}
	}

}
