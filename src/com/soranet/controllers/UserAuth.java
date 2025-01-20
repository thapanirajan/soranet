package com.soranet.controllers;

import com.soranet.model.UserModel;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages user authentication and registration. It handles login,
 * sign-up, checking user roles (admin), and retrieving user data.
 */
public class UserAuth {

    private final String Admin_username = "admin";
    private final String Admin_password = "admin";

    private final String default_username = "alex";
    private final String default_password = "alex123";
    private final String default_email = "alex@gmail.com";

    private String loggedInUsername = null;

    private final List<UserModel> users = new ArrayList<>();

    /**
     * Constructor initializes the system with default users (admin and one
     * regular user).
     */
    public UserAuth() {
        // Add default admin and user to the list
        users.add(new UserModel(Admin_username, Admin_password));
        users.add(new UserModel(default_username, default_password, default_email));
    }

    /**
     * Handles user sign-up by checking if the username already exists. If not,
     * the new user is added to the system.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @param email The email provided by the user.
     * @return true if sign-up is successful, false if the username already
     * exists.
     */
    public boolean signup(String username, String password, String email) {
        // Check if the username already exists
        if (getUserByUsername(username) != null) {
            return false; // Username already exists
        }
        loggedInUsername = username;
        // Add new user to the system
        users.add(new UserModel(username, password, email));
        return true;
    }

    /**
     * Handles user login by verifying the username and password.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return true if login is successful, otherwise false.
     */
    public boolean login(String username, String password) {
        UserModel user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUsername = username;
            return true;
        }
        return false;
    }

    /**
     * Returns the total number of users in the system.
     *
     * @return The total number of users.
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Checks if the given username belongs to the admin.
     *
     * @param username The username to check.
     * @return true if the username matches the admin username, otherwise false.
     */
    public boolean isAdmin(String username) {
        return Admin_username.equals(username);
    }

    /**
     * Retrieves the username of the currently logged-in user.
     *
     * @return The username of the logged-in user.
     */
    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    /**
     * Logs out the currently logged-in user by setting the loggedInUsername to
     * null.
     */
    public void logOut() {
        loggedInUsername = null;
    }

    /**
     * Helper method to find a user by their username.
     *
     * @param username The username to search for.
     * @return The UserModel if the user is found, otherwise null.
     */
    public UserModel getUserByUsername(String username) {
        for (UserModel user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
