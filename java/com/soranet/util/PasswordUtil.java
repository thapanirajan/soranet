package com.soranet.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
public class PasswordUtil {

	private static final int LOG_ROUNDS = 12; // Work factor for BCrypt

	/**
	 * Hashes a password using BCrypt.
	 *
	 * @param password The plaintext password to hash
	 * @return The hashed password
	 */
	public static String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
	}

	/**
	 * Verifies a plaintext password against a hashed password.
	 *
	 * @param password       The plaintext password to verify
	 * @param hashedPassword The hashed password to verify against
	 * @return True if the password matches, false otherwise
	 */
	public static boolean verifyPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}
}
