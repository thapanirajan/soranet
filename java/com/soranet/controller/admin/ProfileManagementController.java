package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

import com.soranet.model.UserModel;
import com.soranet.service.auth.UserService;
import com.soranet.util.ImageUtil;
import com.soranet.util.SessionUtil;
import com.soranet.util.ValidationUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/admin/profile" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
		maxFileSize = 1024 * 1024 * 5, // 5MB
		maxRequestSize = 1024 * 1024 * 5 // 5MB
)
public class ProfileManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ImageUtil imageUtil = new ImageUtil();
	private UserService userService = new UserService();

	private static final String ABSOLUTE_WORKSPACE_WEBAPP_PATH = "C:/Users/thapa/eclipse-workspace/soranet/src/main/webapp";
	private static final String PROFILE_PICS_SUBFOLDER = "/profile_pictures";
	private static final String DEFAULT_PROFILE_PICTURE_DB_PATH = ImageUtil.WEB_RELATIVE_UPLOAD_DIR_ROOT
			+ PROFILE_PICS_SUBFOLDER + "/" + ImageUtil.GENERIC_DEFAULT_IMAGE_FILENAME;

	public ProfileManagementController() {
		super();
	}

	/**
	 * Handles GET requests to display the admin profile page. Verifies admin
	 * access, retrieves the admin's profile data, and forwards to the
	 * profileManagement JSP. Redirects to login if the user is not found or not an
	 * admin.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check if user is admin
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. You must be an admin.");
			return;
		}

		UserModel sessionUser = (UserModel) SessionUtil.getAttribute(request, "user");
		try {
			// Fetch admin profile from database
			UserModel adminUserData = userService.getUserByUsername(sessionUser.getUsername());
			if (adminUserData == null) {
				request.setAttribute("errorMessage", "Admin profile not found.");
				SessionUtil.removeAttribute(request, "user");
				response.sendRedirect(request.getContextPath() + "/login");
				return;
			}
			// Set user data for JSP
			request.setAttribute("user", adminUserData);
			request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Error loading admin profile: " + e.getMessage());
			request.setAttribute("user", sessionUser);
			request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
		}
	}

	/**
	 * Handles POST requests to update the admin's profile. Validates input fields
	 * and profile picture, updates the database, and handles image uploads.
	 * Forwards to the profileManagement JSP with success or error messages.
	 *
	 * @param request  the HttpServletRequest object containing form data and file
	 *                 upload
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Verify admin access
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. You must be an admin.");
			return;
		}

		UserModel sessionUser = (UserModel) SessionUtil.getAttribute(request, "user");
		String username = sessionUser.getUsername();
		UserModel currentAdminData = null;

		try {
			// Retrieve current admin data
			currentAdminData = userService.getUserByUsername(username);
			if (currentAdminData == null) {
				request.setAttribute("errorMessage", "Admin profile not found. Please log in again.");
				SessionUtil.removeAttribute(request, "user");
				response.sendRedirect(request.getContextPath() + "/login");
				return;
			}

			// Extract form parameters
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			Part profilePicturePart = request.getPart("profilePicture");

			String newProfilePictureDbPath = currentAdminData.getProfilePicture();
			if (newProfilePictureDbPath == null || newProfilePictureDbPath.trim().isEmpty()) {
				newProfilePictureDbPath = DEFAULT_PROFILE_PICTURE_DB_PATH;
			}

			// Validate input fields
			String validationError = null;
			if (ValidationUtil.isNullOrEmpty(firstName))
				validationError = "First name is required.";
			else if (ValidationUtil.isNullOrEmpty(lastName))
				validationError = "Last name is required.";
			else if (ValidationUtil.isNullOrEmpty(email))
				validationError = "Email is required.";
			else if (!ValidationUtil.isValidEmail(email))
				validationError = "Invalid email format.";

			if (phoneNumber != null && !phoneNumber.isEmpty() && !ValidationUtil.isValidPhoneNumber(phoneNumber)) {
				validationError = "Phone number, if provided, must be 10 digits starting with 98.";
			}

			if (validationError == null) {
				UserModel existingUserByEmail = UserService.getUserByLoginId(email);
				if (existingUserByEmail != null && !existingUserByEmail.getUsername().equals(username)) {
					validationError = "Email is already in use by another user.";
				}
			}

			boolean newImageProvided = (profilePicturePart != null && profilePicturePart.getSize() > 0
					&& profilePicturePart.getSubmittedFileName() != null
					&& !profilePicturePart.getSubmittedFileName().isEmpty());

			// Validate profile picture
			if (newImageProvided) {
				if (!ValidationUtil.isValidImageExtension(profilePicturePart)) {
					validationError = "Profile picture must be a JPG, JPEG, PNG, or GIF file.";
				} else if (profilePicturePart.getSize() > 5 * 1024 * 1024) {
					validationError = "Profile picture file size must be less than 5MB.";
				}
			}

			if (validationError != null) {
				request.setAttribute("errorMessage", validationError);
				request.setAttribute("user", currentAdminData);
				currentAdminData.setFirstName(firstName);
				currentAdminData.setLastName(lastName);
				currentAdminData.setEmail(email);
				currentAdminData.setPhoneNumber(phoneNumber);
				currentAdminData.setAddress(address);
				currentAdminData.setCity(city);
				request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
				return;
			}

			// Handle profile picture upload
			if (newImageProvided) {
				String uploadedRelativePath = imageUtil.uploadImageToWorkspace(profilePicturePart,
						ABSOLUTE_WORKSPACE_WEBAPP_PATH, PROFILE_PICS_SUBFOLDER);
				if (uploadedRelativePath != null) {
					if (currentAdminData.getProfilePicture() != null && !currentAdminData.getProfilePicture().isEmpty()
							&& !currentAdminData.getProfilePicture().equals(DEFAULT_PROFILE_PICTURE_DB_PATH)
							&& !currentAdminData.getProfilePicture().equals(uploadedRelativePath)) {
						// Previous image deletion is commented out in original code
						/*
						 * imageUtil.deleteImageFromWorkspace(ABSOLUTE_WORKSPACE_WEBAPP_PATH,
						 * currentAdminData.getProfilePicture());
						 */
					}
					newProfilePictureDbPath = uploadedRelativePath;
				} else {
					if (profilePicturePart.getSize() > 0) {
						request.setAttribute("warningMessage",
								"Could not upload new profile picture due to an internal error. Previous picture retained.");
					}
				}
			}

			// Update user profile in database
			boolean updateSuccess = userService.updateUser(username, firstName, lastName, email, phoneNumber, address,
					city, newProfilePictureDbPath);

			if (updateSuccess) {
				// Refresh session with updated data
				UserModel updatedAdmin = userService.getUserByUsername(username);
				SessionUtil.setAttribute(request, "user", updatedAdmin);
				request.setAttribute("successMessage", "Admin profile updated successfully.");
				request.setAttribute("user", updatedAdmin);
			} else {
				request.setAttribute("errorMessage", "Failed to update admin profile in the database.");
				request.setAttribute("user", currentAdminData);
			}
			request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
			request.setAttribute("user", currentAdminData != null ? currentAdminData : sessionUser);
			request.getRequestDispatcher("/WEB-INF/views/admin/profileManagement.jsp").forward(request, response);
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
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}
}