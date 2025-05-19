package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.soranet.model.UserModel;
import com.soranet.service.auth.AuthService;
import com.soranet.util.CookieUtil;
import com.soranet.util.PasswordUtil;
import com.soranet.util.SessionUtil;

/**
 * Servlet for handling user login. Handles GET requests to display the login
 * page or redirect authenticated users, and POST requests to process login
 * credentials.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LoginController() {
		super();
	}

	/**
	 * Handles GET requests to display the login page or redirect authenticated
	 * users. Checks for existing session or authentication token to redirect users
	 * to appropriate dashboards.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check for existing user session
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");

		if (user == null) {
			// Check for authentication token in cookie
			Cookie authCookie = CookieUtil.getCookie(request, "authToken");
			if (authCookie != null) {
				try {
					AuthService authService = new AuthService();
					user = authService.validateToken(authCookie.getValue());
					if (user != null) {
						// Set user in session
						SessionUtil.setAttribute(request, "user", user);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (user != null) {
			// Redirect based on user role
			String redirectPath = switch (user.getRole().toLowerCase()) {
			case "admin" -> "/admin/dashboard";
			case "customer" -> "/";
			default -> "/WEB-INF/views/customer/login.jsp";
			};
			response.sendRedirect(request.getContextPath() + redirectPath);
			return;
		}

		// Forward to login page
		request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests to process user login. Validates login credentials,
	 * generates authentication token, sets session, and redirects to appropriate
	 * page upon successful login.
	 *
	 * @param request  the HttpServletRequest object containing form data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Extract login parameters
			String loginId = request.getParameter("loginId");
			String password = request.getParameter("password");
			String rememberMe = request.getParameter("rememberMe");

			// Validate input fields
			if (loginId == null || loginId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
				request.setAttribute("message", "Please fill in all required fields.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}

			// Authenticate user
			AuthService authService = new AuthService();
			UserModel user = authService.getUserByLoginId(loginId);
			if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
				request.setAttribute("message", "Invalid username or password.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}

			// Generate and store authentication token
			int expiryDays = "on".equals(rememberMe) ? 30 : 1;
			String token = authService.generateAndStoreToken(user.getUserId(), expiryDays);
			CookieUtil.addCookie(response, "authToken", token, expiryDays * 24 * 60 * 60);

			// Set user in session
			SessionUtil.setAttribute(request, "user", user);

			// Redirect to login for role-based redirection
			response.sendRedirect(request.getContextPath() + "/login");
		} catch (Exception e) {
			// Handle errors and forward to login page
			request.setAttribute("message", "Login failed: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
		}
	}
}