package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

import com.soranet.model.UserModel;
import com.soranet.service.AuthService;
import com.soranet.util.PasswordUtil;
import com.soranet.util.SessionUtil;

/**
 * Servlet implementation class RegisterController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig
public class RegisterController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AuthService authService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterController() {
		super();
	}

	/**
	 * Initializes the servlet and creates an instance of RegisterService.
	 */
	@Override
	public void init() throws ServletException {
		authService = new AuthService();
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
			String role = request.getParameter("role");
			String username = request.getParameter("username");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String password = request.getParameter("password");
			String confirmPassword = request.getParameter("confirm_password");
			Part profilePicturePart = request.getPart("profilePicture");
			String profilePicture = (profilePicturePart != null && profilePicturePart.getSize() > 0)
					? profilePicturePart.getSubmittedFileName()
					: null;

			// Validate password and confirm_password match
			if (!password.equals(confirmPassword)) {
				request.setAttribute("message", "Passwords do not match.");
				request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
				return;
			}

			// Validate phone number (must be exactly 10 digits)
			if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
				request.setAttribute("message", "Phone number must be exactly 10 digits.");
				request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
				return;
			}

			// Hash the password
			String hashedPassword = PasswordUtil.hashPassword(password);

			// Create UserModel object
			UserModel user = new UserModel(0, firstName, lastName, email, phoneNumber, username, hashedPassword, role,
					address, city, profilePicture);

			// Save user via RegisterService
			authService.registerUser(user);

			// Store username in session
			SessionUtil.setAttribute(request, "user", user);

		} catch (Exception e) {
			request.setAttribute("message", "Registration failed: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
		}
	}
}
