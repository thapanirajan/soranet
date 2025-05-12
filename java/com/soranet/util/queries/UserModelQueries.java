package com.soranet.util.queries;

public class UserModelQueries {
	// UserModel Queries
    public static final String INSERT_USER = 
        "INSERT INTO Users (Username, Password, Role, FirstName, LastName, Email, PhoneNumber, Address, City, ProfilePicture) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SELECT_USER_BY_ID = 
        "SELECT * FROM User WHERE UserId = ?";
    
    public static final String SELECT_USER_BY_USERNAME = 
        "SELECT * FROM User WHERE Username = ?";
    
    public static final String SELECT_ALL_USERS = 
        "SELECT * FROM User ORDER BY CreatedAt DESC";
    
    public static final String UPDATE_USER = 
        "UPDATE User SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ?, City = ?, ProfilePicture = ?, UpdatedAt = CURRENT_TIMESTAMP " +
        "WHERE UserId = ?";
    
    public static final String DELETE_USER = 
        "DELETE FROM Users WHERE UserId = ?";
    
    public static final String COUNT_TOTAL_USERS = 
        "SELECT COUNT(*) AS total_users FROM User";
    
    public static final String COUNT_NEW_USERS = 
        "SELECT COUNT(*) AS new_users FROM User WHERE CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
    
    public static final String SELECT_RECENT_USERS = 
        "SELECT UserId, Username, Email, CreatedAt FROM User ORDER BY CreatedAt DESC LIMIT 5";
    
    public static final String COUNT_USER_BY_USERNAME = "SELECT COUNT(*) FROM Users WHERE Username = ?";
    
    public static final String COUNT_USER_BY_EMAIL = "SELECT COUNT(*) FROM Users WHERE Email = ?";
}
