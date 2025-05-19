package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDateTime;

import com.soranet.model.UserModel;
import com.soranet.service.auth.AuthService;
import com.soranet.service.auth.UserService;
import com.soranet.util.CookieUtil;
import com.soranet.util.ImageUtil;
import com.soranet.util.PasswordUtil;
import com.soranet.util.SessionUtil;
import com.soranet.util.ValidationUtil;

/**
 * Servlet for handling user registration. Handles GET requests to display the
 * registration page and POST requests to process user registration.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
		maxFileSize = 1024 * 1024 * 5, // 5MB
		maxRequestSize = 1024 * 1024 * 5 // 5MB
)
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ImageUtil imageUtil = new ImageUtil();

	private static final String ABSOLUTE_WORKSPACE_WEBAPP_PATH = "C:/Users/thapa/eclipse-workspace/soranet/src/main/webapp";
	private static final String PROFILE_PICS_SUBFOLDER = "/profile_pictures";
	private static final String DEFAULT_PROFILE_PICTURE_DB_PATH = ImageUtil.WEB_RELATIVE_UPLOAD_DIR_ROOT
			+ PROFILE_PICS_SUBFOLDER + "/" + ImageUtil.GENERIC_DEFAULT_IMAGE_FILENAME;

	/**
	 * Default constructor.
	 */
	public RegisterController() {
		super();
	}

	/**
	 * Handles GET requests to display the registration page. Clears any existing
	 * error messages and forwards to the registration JSP.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Clear error messages
		request.removeAttribute("errorMessage");
		// Forward to registration page
		request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests to process user registration. Validates input fields
	 * and profile picture, handles image upload, registers the user, and sets up
	 * authentication token and session upon successful registration.
	 *
	 * @param request  the HttpServletRequest object containing form data and file
	 *                 upload
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Extract form parameters
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");
			String username = request.getParameter("username");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String password = request.getParameter("password");
			String confirmPassword = request.getParameter("confirm_password");
			Part profilePicturePart = request.getPart("profilePicture");

			String errorMessage = null;

			// Validate input fields
			if (ValidationUtil.isNullOrEmpty(firstName)) {
				errorMessage = "First name is required.";
			} else if (ValidationUtil.isNullOrEmpty(lastName)) {
				errorMessage = "Last name is required.";
			} else if (ValidationUtil.isNullOrEmpty(email)) {
				errorMessage = "Email is required.";
			} else if (!ValidationUtil.isValidEmail(email)) {
				errorMessage = "Invalid email format.";
			} else {
				AuthService authService = new AuthService();
				// Check for existing email
				if (authService.getUserByLoginId(email) != null) {
					errorMessage = "This email address is already registered.";
				}
			}
			if (ValidationUtil.isNullOrEmpty(phoneNumber)) {
				errorMessage = "Phone number is required.";
			} else if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
				errorMessage = "Phone number must be 10 digits starting with 98.";
			} else if (ValidationUtil.isNullOrEmpty(username)) {
				errorMessage = "Username is required.";
			} else if (!ValidationUtil.isAlphanumericStartingWithLetter(username)) {
				errorMessage = "Username must start with a letter and contain only letters and numbers.";
			} else {
				UserService userService = new UserService();
				// Check for existing username
				if (userService.getUserByUsername(username) != null) {
					errorMessage = "This username is already taken.";
				}
			}
			if (ValidationUtil.isNullOrEmpty(password)) {
				errorMessage = "Password is required.";
			} else if (!ValidationUtil.isValidPassword(password)) {
				errorMessage = "Password must be at least 8 characters, with 1 capital letter, 1 number, and 1 symbol.";
			} else if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
				errorMessage = "Passwords do not match.";
			}

			// Validate profile picture
			boolean newImageProvided = (profilePicturePart != null && profilePicturePart.getSize() > 0
					&& profilePicturePart.getSubmittedFileName() != null
					&& !profilePicturePart.getSubmittedFileName().isEmpty());
			if (errorMessage == null && newImageProvided) {
				if (!ValidationUtil.isValidImageExtension(profilePicturePart)) {
					errorMessage = "Profile picture must be a JPG, JPEG, PNG, or GIF file.";
				} else if (profilePicturePart.getSize() > 5 * 1024 * 1024) {
					errorMessage = "Profile picture file size must be less than 5MB.";
				}
			}

			if (errorMessage != null) {
				// Return to registration page with error
				request.setAttribute("errorMessage", errorMessage);
				request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
				return;
			}

			// Handle profile picture upload
			String profilePictureDbPath = DEFAULT_PROFILE_PICTURE_DB_PATH;
			if (newImageProvided) {
				String uploadedRelativePath = imageUtil.uploadImageToWorkspace(profilePicturePart,
						ABSOLUTE_WORKSPACE_WEBAPP_PATH, PROFILE_PICS_SUBFOLDER);
				if (uploadedRelativePath != null) {
					profilePictureDbPath = uploadedRelativePath;
				} else {
					System.err.println(
							"RegisterController: Profile picture upload failed, using default. User: " + username);
					request.setAttribute("warningMessage", "Could not upload profile picture, using default image.");
				}
			}

			// Create user model with hashed password
			String hashedPassword = PasswordUtil.hashPassword(password);
			UserModel userToRegister = new UserModel(0, username, hashedPassword, "customer", firstName, lastName,
					email, phoneNumber, address, city, profilePictureDbPath, LocalDateTime.now(), null);

			// Register user
			UserService userService = new UserService();
			boolean registrationSuccess = userService.registerUser(userToRegister);

			if (registrationSuccess) {
				// Retrieve registered user
				UserModel registeredUser = userService.getUserByUsername(username);
				if (registeredUser != null) {
					// Set up authentication token and session
					AuthService authService = new AuthService();
					int expiryDays = 1;
					String token = authService.generateAndStoreToken(registeredUser.getUserId(), expiryDays);
					CookieUtil.addCookie(response, "authToken", token, expiryDays * 24 * 60 * 60);
					SessionUtil.setAttribute(request, "user", registeredUser);

					// Redirect to dashboard
					response.sendRedirect(request.getContextPath() + "/user/dashboard?registration=success");
				} else {
					request.setAttribute("errorMessage",
							"Registration seemed successful, but could not log you in. Please try logging in manually.");
					request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("errorMessage", "Registration failed due to a server error. Please try again.");
				request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Registration failed due to an unexpected error. Please try again.");
			request.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(request, response);
		}
	}
}