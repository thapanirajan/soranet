package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.soranet.model.UserModel;
import com.soranet.service.AuthService;
import com.soranet.util.SessionUtil;

/**
 * Servlet implementation class ProfileManagementController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/profile" })
public class ProfileManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthService authService;

	@Override
	public void init() throws ServletException {
		authService = new AuthService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		request.setAttribute("user", user);
		request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
			String username = user.getUsername();
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");
			String address = request.getParameter("address");
			String city = request.getParameter("city");

			// Validate inputs
			if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || email == null
					|| email.isEmpty()) {
				throw new IllegalArgumentException("Required fields are missing");
			}
			if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				throw new IllegalArgumentException("Invalid email format");
			}

			authService.updateUser(username, firstName, lastName, phoneNumber, address, city, user.getProfilePicture());

			// Update session user
			UserModel updatedUser = authService.getUserByUsername(username);
			SessionUtil.setAttribute(request, "user", updatedUser);

			request.setAttribute("successMessage", "Profile updated successfully");
			request.setAttribute("user", updatedUser);
			request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
		} catch (IllegalArgumentException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("user", SessionUtil.getAttribute(request, "user"));
			request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error updating profile: " + e.getMessage());
			request.setAttribute("user", SessionUtil.getAttribute(request, "user"));
			request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
		}
	}

	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}
}