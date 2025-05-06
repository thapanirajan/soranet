package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDateTime;
/*import java.util.Random;*/

import com.soranet.model.UserModel;
import com.soranet.service.AuthService;
import com.soranet.util.CookieUtil;
import com.soranet.util.PasswordUtil;
import com.soranet.util.SessionUtil;
import com.soranet.util.ValidationUtil;

/**
 * Servlet implementation class RegisterController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig
public class RegisterController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/views/customer/register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Retrieve form data
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");
			/* String role = request.getParameter("role"); */
			String username = request.getParameter("username");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String password = request.getParameter("password");
			String confirmPassword = request.getParameter("confirm_password");
			Part profilePicturePart = request.getPart("profilePicture");
			String profilePicture = (profilePicturePart != null && profilePicturePart.getSize() > 0)
					? profilePicturePart.getSubmittedFileName()
					: null;

			// Initialize error message
			String errorMessage = null;

			// Validate for null or empty fields using ValidationUtil
			if (ValidationUtil.isNullOrEmpty(firstName)) {
				errorMessage = "First name is required.";
			} else if (ValidationUtil.isNullOrEmpty(lastName)) {
				errorMessage = "Last name is required.";
			} else if (ValidationUtil.isNullOrEmpty(email)) {
				errorMessage = "Email is required.";
			} else if (!ValidationUtil.isValidEmail(email)) {
				errorMessage = "Invalid email format.";
			} else if (ValidationUtil.isNullOrEmpty(phoneNumber)) {
				errorMessage = "Phone number is required.";
			} else if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
				errorMessage = "Phone number must be 10 digits starting with 98.";
			} else if (ValidationUtil.isNullOrEmpty(username)) {
				errorMessage = "Username is required.";
			} else if (!ValidationUtil.isAlphanumericStartingWithLetter(username)) {
				errorMessage = "Username must start with a letter and contain only letters and numbers.";
			} else if (ValidationUtil.isNullOrEmpty(address)) {
				errorMessage = "Address is required.";
			} else if (ValidationUtil.isNullOrEmpty(city)) {
				errorMessage = "City is required.";
			} else if (ValidationUtil.isNullOrEmpty(password)) {
				errorMessage = "Password is required.";
			} else if (!ValidationUtil.isValidPassword(password)) {
				errorMessage = "Password must be at least 8 characters, with 1 capital letter, 1 number, and 1 symbol.";
			} else if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
				errorMessage = "Passwords do not match.";
			} else if (profilePicturePart != null && profilePicturePart.getSize() > 0
					&& !ValidationUtil.isValidImageExtension(profilePicturePart)) {
				errorMessage = "Profile picture must be a JPG, JPEG, PNG, or GIF file.";
			}

			// If validation fails, forward back to the registration page with error message
			if (errorMessage != null) {
				request.setAttribute("message", errorMessage);
				request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
				return;
			}

			/*
			 * Random rand = new Random(); int userId = 10000 + rand.nextInt(90000);
			 */

			// Hash the password
			String hashedPassword = PasswordUtil.hashPassword(password);

			// Create UserModel object
			UserModel user = new UserModel(0, username, hashedPassword, "customer", firstName, lastName, email,
					phoneNumber, address, city, profilePicture, LocalDateTime.now(), null);

			// Save user via RegisterService
			AuthService.registerUser(user);

			System.out.println("User ID after createUser: " + user.getUserId());

			int expiryDays = 1;
			String token = AuthService.generateAndStoreToken(user.getUserId(), expiryDays);

			CookieUtil.addCookie(response, "authToken", token, expiryDays * 24 * 60 * 60);

			SessionUtil.setAttribute(request, "user", user);

			response.sendRedirect(request.getContextPath() + "/login");

		} catch (Exception e) {
			request.setAttribute("message", "Registration failed: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
		}
	}
}
