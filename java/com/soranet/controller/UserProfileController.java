package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

import com.soranet.util.ImageUtil;
import com.soranet.util.SessionUtil;
import com.soranet.model.UserModel;
import com.soranet.service.AuthService;
import com.soranet.util.ValidationUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/user/profile" })
public class UserProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ImageUtil imageUtil;
	private static final String SAVE_FOLDER = "Uploads";

	public UserProfileController() {
		super();
	}

	@Override
	public void init() throws ServletException {
		imageUtil = new ImageUtil();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		System.out.println("User from session: " + (user != null ? user.getUsername() : "null"));

		if (user == null) {
			request.setAttribute("message", "Please log in to view your profile.");
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
			return;
		}

		try {
			String username = user.getUsername();
			UserModel userData = AuthService.getUserByUsername(username);
			if (userData == null) {
				request.setAttribute("message", "User not found.");
				request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
				return;
			}
			request.setAttribute("user", userData);
			request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("message", "Error loading profile: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		if (user == null) {
			request.setAttribute("message", "Please log in to update your profile.");
			request.getRequestDispatcher("/WEB-INF/views/customer/login.jsp").forward(request, response);
			return;
		}

		try {
			String username = user.getUsername();
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			Part profilePicturePart = request.getPart("profilePicture");
			String profilePicture = null;

			// Validate required fields
			if (ValidationUtil.isNullOrEmpty(firstName) || ValidationUtil.isNullOrEmpty(lastName)
					|| ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(phoneNumber)) {
				request.setAttribute("message", "Please fill in all required fields.");
				UserModel userData = AuthService.getUserByUsername(username);
				request.setAttribute("user", userData);
				request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
						response);
				return;
			}

			// Validate firstName and lastName (must be alphabetic)
			if (!ValidationUtil.isAlphabetic(firstName)) {
				request.setAttribute("message", "First name must contain only letters.");
				UserModel userData = AuthService.getUserByUsername(username);
				request.setAttribute("user", userData);
				request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
						response);
				return;
			}

			if (!ValidationUtil.isAlphabetic(lastName)) {
				request.setAttribute("message", "Last name must contain only letters.");
				UserModel userData = AuthService.getUserByUsername(username);
				request.setAttribute("user", userData);
				request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
						response);
				return;
			}

			// Validate phone number (must start with 98 and be 10 digits)
			if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
				request.setAttribute("message", "Phone number must start with 98 and be exactly 10 digits.");
				UserModel userData = AuthService.getUserByUsername(username);
				request.setAttribute("user", userData);
				request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
						response);
				return;
			}

			// Validate email format
			if (!ValidationUtil.isValidEmail(email)) {
				request.setAttribute("message", "Invalid email format.");
				UserModel userData = AuthService.getUserByUsername(username);
				request.setAttribute("user", userData);
				request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
						response);
				return;
			}

			// Check if email is already in use by another user
			UserModel existingUser = AuthService.getUserByLoginId(email);
			if (existingUser != null && !existingUser.getUsername().equals(username)) {
				request.setAttribute("message", "Email is already in use by another user.");
				UserModel userData = AuthService.getUserByUsername(username);
				request.setAttribute("user", userData);
				request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
						response);
				return;
			}

			// Handle profile picture upload
			UserModel currentUser = AuthService.getUserByUsername(username);
			if (profilePicturePart != null && profilePicturePart.getSize() > 0) {
				// Validate file type
				if (!ValidationUtil.isValidImageExtension(profilePicturePart)) {
					request.setAttribute("message",
							"Only JPG, JPEG, PNG, and GIF files are allowed for profile picture.");
					request.setAttribute("user", currentUser);
					request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
							response);
					return;
				}

				// Validate file size (max 5MB)
				if (profilePicturePart.getSize() > 5 * 1024 * 1024) {
					request.setAttribute("message", "Profile picture must be less than 5MB.");
					request.setAttribute("user", currentUser);
					request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
							response);
					return;
				}

				// Delete old profile picture if it exists
				if (currentUser.getProfilePicture() != null && !currentUser.getProfilePicture().isEmpty()) {
					String oldFilePath = imageUtil.getSavePath(SAVE_FOLDER) + "/" + currentUser.getProfilePicture();
					File oldFile = new File(oldFilePath);
					if (oldFile.exists()) {
						oldFile.delete();
					}
				}

				// Generate a unique file name to avoid conflicts
				String originalImageName = imageUtil.getImageNameFromPart(profilePicturePart);
				String fileExtension = originalImageName.substring(originalImageName.lastIndexOf("."));
				String uniqueImageName = username + "_" + System.currentTimeMillis() + fileExtension;

				// Upload the image with the unique name
				if (imageUtil.uploadImage(profilePicturePart, "", SAVE_FOLDER)) {
					String uploadedPath = imageUtil.getSavePath(SAVE_FOLDER) + "/" + originalImageName;
					File uploadedFile = new File(uploadedPath);
					File renamedFile = new File(imageUtil.getSavePath(SAVE_FOLDER) + "/" + uniqueImageName);
					if (uploadedFile.exists()) {
						uploadedFile.renameTo(renamedFile);
					}
					profilePicture = uniqueImageName;
				} else {
					request.setAttribute("message", "Failed to upload profile picture.");
					request.setAttribute("user", currentUser);
					request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request,
							response);
					return;
				}
			} else {
				profilePicture = currentUser.getProfilePicture();
			}

			// Update user details in the database
			AuthService.updateUser(username, firstName, lastName, phoneNumber, address, city, profilePicture);

			// Refresh user data and show success message
			UserModel updatedUser = AuthService.getUserByUsername(username);
			SessionUtil.setAttribute(request, "user", updatedUser);
			request.setAttribute("message", "Profile updated successfully.");
			request.setAttribute("user", updatedUser);
			request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("message", "Error updating profile: " + e.getMessage());
			try {
				UserModel userData = AuthService.getUserByUsername(user.getUsername());
				request.setAttribute("user", userData);
			} catch (Exception ex) {
				request.setAttribute("message", "Error loading profile: " + ex.getMessage());
			}
			request.getRequestDispatcher("/WEB-INF/views/customer/profileManagement.jsp").forward(request, response);
		}
	}
}