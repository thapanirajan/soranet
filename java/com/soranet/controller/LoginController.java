package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
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

	private static final long serialVersionUID = 1L;;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");

		if (user == null) {
			Cookie authCookie = CookieUtil.getCookie(request, "authToken");
			if (authCookie != null) {
				try {
					user = AuthService.validateToken(authCookie.getValue());
					if (user != null) {
						SessionUtil.setAttribute(request, "user", user);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (user != null) {
			String redirectPath = switch (user.getRole().toLowerCase()) {
			case "admin" -> "/admin/dashboard";
			case "customer" -> "/";
			default -> "/WEB-INF/views/customer/login.jsp";
			};
			response.sendRedirect(request.getContextPath() + redirectPath);
			return;
		}

		request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String loginId = request.getParameter("loginId");
			String password = request.getParameter("password");
			String rememberMe = request.getParameter("rememberMe");

			// Validate input
			if (loginId == null || loginId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
				request.setAttribute("message", "Please fill in all required fields.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}

			UserModel user = AuthService.getUserByLoginId(loginId);
			if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
				request.setAttribute("message", "Invalid username or password.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}

			int expiryDays = "on".equals(rememberMe) ? 30 : 1;
			String token = AuthService.generateAndStoreToken(user.getUserId(), expiryDays);
			CookieUtil.addCookie(response, "authToken", token, expiryDays * 24 * 60 * 60);

			SessionUtil.setAttribute(request, "user", user);

			response.sendRedirect(request.getContextPath() + "/login");

		} catch (Exception e) {
			request.setAttribute("message", "Login failed: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
		}
	}

}
