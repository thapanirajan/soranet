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

public class AuthService {

	public UserModel getUserByLoginId(String loginId) throws Exception {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.GET_USER_BY_LOGINID)) {
			pstmt.setString(1, loginId);
			pstmt.setString(2, loginId);
			try (ResultSet rs = pstmt.executeQuery()) {
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
							rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime()
									: null,
							rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime()
									: null);
				}
				return null;
			}
		}
	}

	public String generateAndStoreToken(int userId, int expiryDays) throws SQLException, ClassNotFoundException {
		String token = UUID.randomUUID().toString();
		String hashedToken = PasswordUtil.hashPassword(token);

		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.INSERT_AUTH_TOKEN)) {
			pstmt.setInt(1, userId);
			pstmt.setString(2, hashedToken);
			pstmt.setInt(3, expiryDays);
			pstmt.executeUpdate();
			return token;
		}
	}

	public UserModel validateToken(String token) throws SQLException, ClassNotFoundException {
		if (token == null || token.trim().isEmpty()) {
			return null;
		}

		String hashedToken = PasswordUtil.hashPassword(token);
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(UserModelQueries.SELECT_USER_BY_VALID_TOKEN)) {
			pstmt.setString(1, hashedToken);
			try (ResultSet rs = pstmt.executeQuery()) {
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
							rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime()
									: null,
							rs.getTimestamp("UpdatedAt") != null ? rs.getTimestamp("UpdatedAt").toLocalDateTime()
									: null);
				}
				return null;
			}
		}
	}
}