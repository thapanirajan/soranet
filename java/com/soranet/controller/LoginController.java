package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.soranet.model.UserModel;
import com.soranet.util.CookieUtil;
import com.soranet.util.PasswordUtil;
import com.soranet.util.SessionUtil;
import com.soranet.service.AuthService;

/**
 * Servlet implementation class LoginController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AuthService authService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
		super();
	}

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
		request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String loginId = request.getParameter("loginId");
			String password = request.getParameter("password");

			// Validate input
			if (loginId == null || loginId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
				request.setAttribute("message", "Please fill in all required fields.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}

			// Retrieve user from database
			UserModel user = authService.getUserByLoginId(loginId);
			if (user == null) {
				request.setAttribute("message", "Invalid username or password.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}

			// Verify password
			if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
				request.setAttribute("message", "Invalid username or password.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}

			// Session management
			SessionUtil.setAttribute(request, "user", user);

			// Remember Me functionality
			if ("on".equals(request.getParameter("rememberMe"))) {
				String token = AuthService.generateAndStoreToken(user.getUserId());
				CookieUtil.addCookie(response, "rememberToken", token, 30 * 24 * 60 * 60); // 30 days
			}
		} catch (Exception e) {
			request.setAttribute("message", "Login failed: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
		}
	}
}
