package com.soranet.util;

import java.util.regex.Pattern;
import jakarta.servlet.http.Part;

public class ValidationUtil {


	public static boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static boolean isAlphanumericStartingWithLetter(String value) {
		return value != null && value.matches("^[a-zA-Z][a-zA-Z0-9]*$");
	}


	public static boolean isValidEmail(String email) {
		String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		return email != null && Pattern.matches(emailRegex, email);
	}

	
	public static boolean isValidPhoneNumber(String number) {
		return number != null && number.matches("^98\\d{8}$");
	}

	
	public static boolean isValidPassword(String password) {
		String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$";
		return password != null && password.matches(passwordRegex);
	}

	
	public static boolean isValidImageExtension(Part imagePart) {
		if (imagePart == null || isNullOrEmpty(imagePart.getSubmittedFileName())) {
			return false;
		}
		String fileName = imagePart.getSubmittedFileName().toLowerCase();
		return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
				|| fileName.endsWith(".gif");
	}

	
	public static boolean doPasswordsMatch(String password, String retypePassword) {
		return password != null && password.equals(retypePassword);
	}

}

