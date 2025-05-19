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

/**
 * Service class for managing user-related operations. Provides methods for user
 * registration, retrieval, updating, deletion, and searching.
 */
public class UserService {

	/**
	 * Registers a new user in the database. Checks for duplicate username and
	 * email, inserts user data, and retrieves generated user ID and creation
	 * timestamp.
	 *
	 * @param user the UserModel containing user data to register
	 * @return true if registration is successful, false if username or email
	 *         already exists
	 * @throws Exception if a database error occurs or registration fails
	 */
	public boolean registerUser(UserModel user) throws Exception {
		try (Connection conn = DbConfig.getDbConnection()) {
			conn.setAutoCommit(false);

			// Check for existing username
			try (PreparedStatement checkStmt = conn.prepareStatement(UserModelQueries.COUNT_USER_BY_USERNAME)) {
				checkStmt.setString(1, user.getUsername());
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						return false;
					}
				}
			}

			// Check for existing email
			try (PreparedStatement checkStmt = conn.prepareStatement(UserModelQueries.COUNT_USER_BY_EMAIL)) {
				checkStmt.setString(1, user.getEmail());
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						return false;
					}
				}
			}

			// Insert user data
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

				// Retrieve generated user ID
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						user.setUserId(rs.getInt(1));
					} else {
						throw new SQLException("Failed to retrieve generated UserId.");
					}
				}
			}

			// Retrieve creation timestamp
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

			// Commit transaction
			conn.commit();
			return true;
		} catch (SQLException e) {
			throw new Exception("Failed to register user: " + e.getMessage(), e);
		}
	}

	/**
	 * Retrieves a user by their login ID (username or email).
	 *
	 * @param loginId the username or email to search for
	 * @return the UserModel if found, or null if no user matches the login ID
	 * @throws Exception if a database error occurs
	 */
	public static UserModel getUserByLoginId(String loginId) throws Exception {
		// Establish database connection and prepare query
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.GET_USER_BY_LOGINID)) {
			pstmt.setString(1, loginId);
			pstmt.setString(2, loginId);
			// Execute query and process results
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

	/**
	 * Updates a user's profile information in the database.
	 *
	 * @param username       the username of the user to update
	 * @param firstName      the updated first name
	 * @param lastName       the updated last name
	 * @param email          the updated email
	 * @param phoneNumber    the updated phone number
	 * @param address        the updated address
	 * @param city           the updated city
	 * @param profilePicture the updated profile picture path
	 * @return true if the update is successful, false otherwise
	 * @throws Exception if a database error occurs
	 */
	public boolean updateUser(String username, String firstName, String lastName, String email, String phoneNumber,
			String address, String city, String profilePicture) throws Exception {
		// Update user data
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

	/**
	 * Deletes a user and their associated data (subscriptions, auth tokens) from
	 * the database.
	 *
	 * @param userId the ID of the user to delete
	 * @return true if deletion is successful, false if no user is found
	 * @throws ClassNotFoundException if the database driver is not found
	 * @throws SQLException           if a database error occurs
	 */
	public boolean deleteUser(int userId) throws ClassNotFoundException, SQLException {
		try (Connection conn = DbConfig.getDbConnection()) {
			conn.setAutoCommit(false);

			// Delete associated subscriptions and auth tokens
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

			// Commit transaction
			conn.commit();
			return true;
		}
	}

	/**
	 * Updates the role of a user in the database.
	 *
	 * @param userId  the ID of the user to update
	 * @param newRole the new role to assign
	 * @return true if the update is successful, false otherwise
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	public boolean updateUserRole(int userId, String newRole) throws SQLException, ClassNotFoundException {
		// Update user role
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.UPDATE_USER_ROLE_BY_USERID)) {
			pstmt.setString(1, newRole);
			pstmt.setInt(2, userId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	/**
	 * Retrieves all users from the database.
	 *
	 * @return a list of UserModel objects representing all users
	 * @throws ClassNotFoundException if the database driver is not found
	 * @throws SQLException           if a database error occurs
	 */
	public List<UserModel> getAllUsers() throws ClassNotFoundException, SQLException {
		List<UserModel> users = new ArrayList<>();
		// Fetch all users
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SELECT_ALL_USERS);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				// Construct UserModel for each user
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

	/**
	 * Retrieves a user by their username.
	 *
	 * @param username the username to search for
	 * @return the UserModel if found, or null if no user matches the username
	 * @throws Exception if a database error occurs
	 */
	public UserModel getUserByUsername(String username) throws Exception {
		// Fetch user by username
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

	/**
	 * Searches for users by name or email using a partial match.
	 *
	 * @param query the search term to match against first name, last name, or email
	 * @return a list of UserModel objects matching the search criteria
	 * @throws ClassNotFoundException if the database driver is not found
	 * @throws SQLException           if a database error occurs
	 */
	public List<UserModel> searchUsersByNameOrEmail(String query) throws ClassNotFoundException, SQLException {
		List<UserModel> users = new ArrayList<>();
		// Search users with partial match
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SEARCH_USERS_BY_NAME_OR_EMAIL)) {
			String searchPattern = "%" + query.toLowerCase() + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// Construct UserModel for each matching user
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

	/**
	 * Deletes all subscriptions associated with a user.
	 *
	 * @param userId the ID of the user whose subscriptions are to be deleted
	 * @param conn   the database connection to use
	 * @throws SQLException if a database error occurs
	 */
	private void deleteSubscriptionsByUserId(int userId, Connection conn) throws SQLException {
		// Delete user subscriptions
		try (PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.DELETE_SUBSCRIPTIONS_BY_USERID)) {
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Deletes all authentication tokens associated with a user.
	 *
	 * @param userId the ID of the user whose auth tokens are to be deleted
	 * @throws SQLException           if a database error occurs
	 * @throws ClassNotFoundException if the database driver is not found
	 */
	public void deleteAuthTokensById(int userId) throws SQLException, ClassNotFoundException {
		// Delete user auth tokens
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.DELETE_AUTHTOKENS_BY_USERID)) {
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		}
	}
}