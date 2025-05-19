package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import com.soranet.model.UserModel;
import com.soranet.service.auth.UserService;

/**
 * Servlet for handling user logout. Handles GET requests to log out users and
 * POST requests by delegating to GET handling.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/logout" })
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();

	/**
	 * Default constructor.
	 */
	public LogoutController() {
		super();
	}

	/**
	 * Handles GET requests to process user logout. Deletes authentication tokens,
	 * invalidates the session, clears cookies, and forwards to the login page.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Retrieve and invalidate session
			HttpSession session = request.getSession(false);
			UserModel user = (UserModel) session.getAttribute("user");
			int userId = user.getUserId();
			// Delete authentication tokens
			userService.deleteAuthTokensById(userId);
			if (session != null) {
				session.invalidate();
			}

			// Clear all cookies
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
			// Forward to login page
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles POST requests by delegating to doGet. No additional processing is
	 * performed for POST requests.
	 *
	 * @param request  the HttpServletRequest object containing form data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Delegate to doGet for consistent behavior
		doGet(request, response);
	}
}