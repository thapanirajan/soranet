package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.soranet.model.UserModel;
import com.soranet.service.AdminService;
import com.soranet.util.SessionUtil;

/**
 * Servlet implementation class UserManagementController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/users" })
public class UserManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminService adminService;

	@Override
	public void init() throws ServletException {
		adminService = new AdminService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserManagementController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			List<UserModel> users = adminService.getAllUsers();
			request.setAttribute("users", users);
			request.getRequestDispatcher("/WEB-INF/views/admin/userManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error loading users: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			int userId = Integer.parseInt(request.getParameter("userId"));
			String newRole = request.getParameter("role");

			// Validate role
			if (!"customer".equalsIgnoreCase(newRole) && !"admin".equalsIgnoreCase(newRole)) {
				throw new IllegalArgumentException("Invalid role specified");
			}

			if (adminService.updateUserRole(userId, newRole)) {
				request.setAttribute("successMessage", "User role updated successfully");
			} else {
				request.setAttribute("errorMessage", "Failed to update user role");
			}
			doGet(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid user ID");
			doGet(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error updating user: " + e.getMessage());
			doGet(request, response);
		}
	}

	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}
}
