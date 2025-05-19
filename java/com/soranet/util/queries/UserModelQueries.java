package com.soranet.util.queries;

public class UserModelQueries {

	public static final String COUNT_USER_BY_USERNAME = "SELECT COUNT(*) FROM Users WHERE Username = ?";

	public static final String COUNT_USER_BY_EMAIL = "SELECT COUNT(*) FROM Users WHERE Email = ?";

	public static final String INSERT_USER = "INSERT INTO Users (Username, Password, Role, FirstName, LastName, Email, PhoneNumber, Address, City, ProfilePicture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String SELECT_CREATED_AT_BY_USERID = "SELECT CreatedAt FROM Users WHERE UserId = ?";

	public static final String GET_USER_BY_LOGINID = "SELECT * FROM Users WHERE Username = ? OR Email = ?";

	public static final String UPDATE_USER_BY_USERNAME = "UPDATE Users SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ?, City = ?, ProfilePicture = ?, UpdatedAt = CURRENT_TIMESTAMP WHERE Username = ?";

	public static final String DELETE_USER_BY_USERID = "DELETE FROM Users WHERE UserId = ?";

	public static final String UPDATE_USER_ROLE_BY_USERID = "UPDATE Users SET Role = ?, UpdatedAt = CURRENT_TIMESTAMP WHERE UserId = ?";

	public static final String SELECT_ALL_USERS = "SELECT * FROM Users";

	public static final String SELECT_USER_BY_USERNAME = "SELECT * FROM Users WHERE Username = ?";

	public static final String DELETE_SUBSCRIPTIONS_BY_USERID = "DELETE FROM Subscription WHERE UserId = ?";

	public static final String DELETE_AUTHTOKENS_BY_USERID = "DELETE FROM AuthToken WHERE UserId = ?";

	public static final String SELECT_USER_BY_VALID_TOKEN = "SELECT u.* FROM AuthToken t JOIN Users u ON t.UserId = u.UserId WHERE t.Token = ? AND t.ExpiryDate > NOW()";

	public static final String INSERT_AUTH_TOKEN = "INSERT INTO AuthToken (UserId, Token, ExpiryDate) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL ? DAY))";

	public static final String SEARCH_USERS_BY_NAME_OR_EMAIL = "SELECT * FROM Users WHERE LOWER(FirstName) LIKE ? OR LOWER(LastName) LIKE ? OR LOWER(Email) LIKE ?";
}
