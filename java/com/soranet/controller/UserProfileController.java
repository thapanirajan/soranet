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
import com.soranet.service.auth.AuthService;
import com.soranet.service.auth.UserService;
import com.soranet.util.ImageUtil;
import com.soranet.util.SessionUtil;
import com.soranet.util.ValidationUtil;

/**
 * Servlet for managing user profile operations. Handles GET requests to display
 * the user profile and POST requests to update profile information.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/user/profile" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
		maxFileSize = 1024 * 1024 * 5, // 5MB
		maxRequestSize = 1024 * 1024 * 5 // 5MB
)
public class UserProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ImageUtil imageUtil = new ImageUtil();

	private static final String ABSOLUTE_WORKSPACE_WEBAPP_PATH = "C:/Users/thapa/eclipse-workspace/soranet/src/main/webapp";
	private static final String PROFILE_PICS_SUBFOLDER = "/profile_pictures";
	private static final String DEFAULT_PROFILE_PICTURE_DB_PATH = ImageUtil.WEB_RELATIVE_UPLOAD_DIR_ROOT
			+ PROFILE_PICS_SUBFOLDER + "/" + ImageUtil.GENERIC_DEFAULT_IMAGE_FILENAME;

	/**
	 * Default constructor.
	 */
	public UserProfileController() {
		super();
	}

	/**
	 * Handles GET requests to display the user profile page. Verifies user session,
	 * retrieves profile data from the database, and forwards to the profile
	 * management JSP.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check for active user session
		UserModel sessionUser = (UserModel) SessionUtil.getAttribute(request, "user");

		if (sessionUser == null) {
			request.setAttribute("errorMessage", "Please log in to view your profile.");
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
			return;
		}

		try {
			// Retrieve user data from database
			UserService userService = new UserService();
			UserModel userDataFromDb = userService.getUserByUsername(sessionUser.getUsername());
			if (userDataFromDb == null) {
				request.setAttribute("errorMessage", "User profile not found. Please try logging in again.");
				SessionUtil.removeAttribute(request, "user");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}
			// Set user data for JSP
			request.setAttribute("user", userDataFromDb);
			request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Error loading profile: " + e.getMessage());
			request.setAttribute("user", sessionUser);
			request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request, response);
		}
	}

	/**
	 * Handles POST requests to update user profile information. Validates input
	 * fields and profile picture, updates the database, and manages profile picture
	 * uploads.
	 *
	 * @param request  the HttpServletRequest object containing form data and file
	 *                 upload
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Verify user session
		UserModel sessionUser = (UserModel) SessionUtil.getAttribute(request, "user");
		if (sessionUser == null) {
			request.setAttribute("errorMessage", "Your session has expired. Please log in again.");
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
			return;
		}

		String username = sessionUser.getUsername();
		UserModel currentUserData = null;

		try {
			// Retrieve current user data
			UserService userService = new UserService();
			currentUserData = userService.getUserByUsername(username);
			if (currentUserData == null) {
				request.setAttribute("errorMessage", "User profile not found. Please try logging in again.");
				SessionUtil.removeAttribute(request, "user");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
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

			String newProfilePictureDbPath = currentUserData.getProfilePicture();
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
			else if (ValidationUtil.isNullOrEmpty(phoneNumber))
				validationError = "Phone number is required.";
			else if (!ValidationUtil.isValidPhoneNumber(phoneNumber))
				validationError = "Phone number must be 10 digits starting with 98.";

			if (validationError == null) {
				// Check for email uniqueness
				AuthService authService = new AuthService();
				UserModel existingUserByEmail = authService.getUserByLoginId(email);
				if (existingUserByEmail != null && !existingUserByEmail.getUsername().equals(username)) {
					validationError = "Email is already in use by another user.";
				}
			}

			// Validate profile picture
			boolean newImageProvided = (profilePicturePart != null && profilePicturePart.getSize() > 0
					&& profilePicturePart.getSubmittedFileName() != null
					&& !profilePicturePart.getSubmittedFileName().isEmpty());

			if (newImageProvided) {
				if (!ValidationUtil.isValidImageExtension(profilePicturePart)) {
					validationError = "Profile picture must be a JPG, JPEG, PNG, or GIF file.";
				} else if (profilePicturePart.getSize() > 5 * 1024 * 1024) {
					validationError = "Profile picture file size must be less than 5MB.";
				}
			}

			if (validationError != null) {
				// Repopulate form and return with error
				request.setAttribute("errorMessage", validationError);
				request.setAttribute("user", currentUserData);
				currentUserData.setFirstName(firstName);
				currentUserData.setLastName(lastName);
				currentUserData.setEmail(email);
				currentUserData.setPhoneNumber(phoneNumber);
				currentUserData.setAddress(address);
				currentUserData.setCity(city);
				request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
						response);
				return;
			}

			// Handle profile picture upload
			if (newImageProvided) {
				String uploadedRelativePath = imageUtil.uploadImageToWorkspace(profilePicturePart,
						ABSOLUTE_WORKSPACE_WEBAPP_PATH, PROFILE_PICS_SUBFOLDER);
				if (uploadedRelativePath != null) {
					if (currentUserData.getProfilePicture() != null && !currentUserData.getProfilePicture().isEmpty()
							&& !currentUserData.getProfilePicture().equals(DEFAULT_PROFILE_PICTURE_DB_PATH)
							&& !currentUserData.getProfilePicture().equals(uploadedRelativePath)) {
						// Previous image deletion is commented out
						// imageUtil.deleteImageFromWorkspace(ABSOLUTE_WORKSPACE_WEBAPP_PATH,
						// currentUserData.getProfilePicture());
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
				UserModel updatedUser = userService.getUserByUsername(username);
				SessionUtil.setAttribute(request, "user", updatedUser);
				request.setAttribute("successMessage", "Profile updated successfully.");
				request.setAttribute("user", updatedUser);
			} else {
				request.setAttribute("errorMessage", "Failed to update profile in the database.");
				request.setAttribute("user", currentUserData);
			}
			// Forward to profile page
			request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage",
					"An unexpected error occurred while updating profile: " + e.getMessage());
			request.setAttribute("user", currentUserData != null ? currentUserData : sessionUser);
			request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request, response);
		}
	}
}