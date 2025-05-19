package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.soranet.model.UserModel;
import com.soranet.service.auth.UserService;
import com.soranet.util.PasswordUtil;
import com.soranet.util.SessionUtil;

/**
 * Servlet for managing users in the admin panel. Handles GET requests to
 * display users and POST requests to create, update, or delete users.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/users" })
public class UserManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;

	/**
	 * Initializes the servlet and sets up the UserService instance.
	 *
	 * @throws ServletException if an error occurs during initialization
	 */
	@Override
	public void init() throws ServletException {
		userService = new UserService();
	}

	/**
	 * Handles GET requests to display the user management page. Verifies admin
	 * access, retrieves users based on search query or fetches all, and forwards to
	 * the userManagement JSP.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Verify admin access
		if (!isAdmin(request)) {
			System.out.println("UserManagementController: isAdmin failed, redirecting to login");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		try {
			List<UserModel> users;
			String searchQuery = request.getParameter("searchQuery");
			if (searchQuery != null && !searchQuery.trim().isEmpty()) {
				// Search users by name or email
				users = userService.searchUsersByNameOrEmail(searchQuery);
			} else {
				// Fetch all users
				users = userService.getAllUsers();
			}
			// Set users for JSP
			request.setAttribute("users", users);
			request.getRequestDispatcher("/WEB-INF/views/admin/userManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error loading users: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/components/error.jsp").forward(request, response);
		}
	}

	/**
	 * Handles POST requests to create, update, or delete a user. Supports creating
	 * new users, updating user roles, or deleting users based on the request
	 * method. Validates inputs and redisplays the user management page with success
	 * or error messages.
	 *
	 * @param request  the HttpServletRequest object containing form data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Verify admin access
		if (!isAdmin(request)) {
			System.out.println("UserManagementController: isAdmin failed, redirecting to login");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		try {
			String method = request.getParameter("_method");
			if ("DELETE".equalsIgnoreCase(method)) {
				// Handle user deletion
				int userId = Integer.parseInt(request.getParameter("userId"));
				System.out.println("User id: " + userId);
				boolean success = userService.deleteUser(userId);
				request.setAttribute("successMessage", success ? "User deleted successfully" : "Failed to delete user");
				doGet(request, response);
				return;
			}

			// Handle create or update user
			int userId = Integer.parseInt(request.getParameter("userId"));
			if (userId == 0) {
				// Create new user
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String email = request.getParameter("email");
				String role = request.getParameter("role");
				String phoneNumber = request.getParameter("phoneNumber");
				String address = request.getParameter("address");
				String city = request.getParameter("city");

				// Validate required fields
				if (username == null || username.isEmpty() || password == null || password.isEmpty()
						|| firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()
						|| email == null || email.isEmpty() || role == null || role.isEmpty()) {
					request.setAttribute("errorMessage", "Required fields are missing");
				}
				// Validate role
				if (!"customer".equalsIgnoreCase(role) && !"admin".equalsIgnoreCase(role)) {
					request.setAttribute("errorMessage", "Invalid role");
				}
				// Hash password for security
				String hashedPassword = PasswordUtil.hashPassword(password);

				// Create user model
				UserModel user = new UserModel(0, username, hashedPassword, role, firstName, lastName, email,
						phoneNumber, address, city);

				// Register new user
				boolean success = userService.registerUser(user);
				request.setAttribute("successMessage", success ? "User created successfully" : "Failed to create user");
			} else {
				// Update user role
				String role = request.getParameter("role");
				if (!"customer".equalsIgnoreCase(role) && !"admin".equalsIgnoreCase(role)) {
					request.setAttribute("errorMessage", "Invalid role");
				}

				// Update role in database
				boolean success = userService.updateUserRole(userId, role);
				request.setAttribute("successMessage",
						success ? "User role updated successfully" : "Failed to update user role");
			}
			// Redisplay user list
			doGet(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid user ID");
			doGet(request, response);
		} catch (IllegalArgumentException e) {
			request.setAttribute("errorMessage", e.getMessage());
			doGet(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error processing user: " + e.getMessage());
			doGet(request, response);
		}
	}

	/**
	 * Checks if the current user is an admin based on session data.
	 *
	 * @param request the HttpServletRequest object containing session data
	 * @return true if the user is an admin, false otherwise
	 */
	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		boolean isAdmin = user != null && "admin".equalsIgnoreCase(user.getRole());
		if (!isAdmin) {
			System.out.println("UserManagementController: isAdmin check failed. User: "
					+ (user != null ? user.getUsername() + ", Role: " + user.getRole() : "null"));
		}
		return isAdmin;
	}
}