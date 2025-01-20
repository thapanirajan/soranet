package com.soranet.model;

/**
 *
 * @author thapa
 */
/**
 * Represents a user in the system, including their credentials and email
 * address. This class provides the structure to store and manipulate user data
 * for both login and signup purposes.
 *
 * Fields: - username (String): The username of the user, used for
 * identification and login. - password (String): The password of the user, used
 * for authentication. - email (String): The email address of the user, used for
 * communication and account management.
 *
 * Methods: - Getters: For accessing the username, password, and email. -
 * Setters: For modifying the username, password, and email.
 *
 * Constructors: - Constructor for signup: Accepts username, password, and email
 * to initialize a new user. - Constructor for login: Accepts only username and
 * password to authenticate the user.
 */
public class UserModel {

    private String username;
    private String password;
    private String email;

    /**
     * Constructor for signing up a new user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param email The email address of the user.
     */
    public UserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructor for logging in an existing user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
