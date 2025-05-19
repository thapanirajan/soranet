package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.soranet.model.SubscriptionModel;
import com.soranet.model.UserModel;
import com.soranet.service.subscription.SubscriptionService;
import com.soranet.util.SessionUtil;

/**
 * Servlet for managing subscriptions in the admin panel. Handles GET requests
 * to display subscriptions and POST requests to create new subscriptions.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/subscriptions" })
public class SubscriptionManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final SubscriptionService subscriptionService = new SubscriptionService();

	/**
	 * Handles GET requests to display the subscription management page. Verifies
	 * admin access, retrieves subscriptions based on search query or fetches all,
	 * and forwards to the subscriptionManagement JSP.
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
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			List<SubscriptionModel> subscriptions;
			String searchQuery = request.getParameter("searchQuery");
			if (searchQuery != null && !searchQuery.trim().isEmpty()) {
				try {
					// Search subscriptions by user or plan ID
					int searchId = Integer.parseInt(searchQuery);
					subscriptions = subscriptionService.getSubscriptionsByUserOrPlanId(searchId);
				} catch (NumberFormatException e) {
					request.setAttribute("errorMessage", "Invalid User ID or Plan ID format");
					// Fallback to all subscriptions
					subscriptions = subscriptionService.getAllSubscriptions();
				}
			} else {
				// Fetch all subscriptions
				subscriptions = subscriptionService.getAllSubscriptions();
			}
			// Set subscriptions for JSP
			request.setAttribute("subscriptions", subscriptions);
			request.getRequestDispatcher("/WEB-INF/views/admin/subscriptionManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error loading subscriptions: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/components/error.jsp").forward(request, response);
		}
	}

	/**
	 * Handles POST requests to create a new subscription. Validates input, checks
	 * user and plan existence, creates the subscription with payment, and
	 * redisplays the subscription management page with success or error messages.
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
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			// Extract and parse form parameters
			int userId = Integer.parseInt(request.getParameter("userId"));
			int planId = Integer.parseInt(request.getParameter("planId"));
			String startDateStr = request.getParameter("startDate");
			String endDateStr = request.getParameter("endDate");
			String paymentMethod = request.getParameter("paymentMethod");

			// Validate required fields
			if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()
					|| paymentMethod == null || paymentMethod.isEmpty()) {
				throw new IllegalArgumentException("Required fields are missing");
			}
			if (userId <= 0 || planId <= 0) {
				throw new IllegalArgumentException("Invalid user ID or plan ID");
			}

			// Verify user existence
			if (!subscriptionService.userExists(userId)) {
				throw new IllegalArgumentException("User with ID " + userId + " does not exist");
			}

			// Verify plan existence
			if (!subscriptionService.planExists(planId)) {
				throw new IllegalArgumentException("Plan with ID " + planId + " does not exist");
			}

			// Parse date inputs
			LocalDate startDate = parseDate(startDateStr);
			LocalDate endDate = parseDate(endDateStr);

			// Create subscription model
			SubscriptionModel subscription = new SubscriptionModel(0, userId, planId, startDate, endDate, null);

			// Create subscription and process payment
			boolean success = subscriptionService.createSubscriptionWithPayment(subscription, paymentMethod);
			request.setAttribute("successMessage",
					success ? "Subscription and payment created successfully" : "Failed to create subscription");

			// Redisplay subscription list
			doGet(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid number format in input");
			doGet(request, response);
		} catch (IllegalArgumentException e) {
			request.setAttribute("errorMessage", e.getMessage());
			doGet(request, response);
		} catch (DateTimeParseException e) {
			request.setAttribute("errorMessage", "Invalid date format. Please use yyyy-MM-dd.");
			doGet(request, response);
		} catch (Exception e) {
			System.err.println("SubscriptionManagementController: Error creating subscription: " + e.getMessage());
			request.setAttribute("errorMessage", "Error creating subscription: " + e.getMessage());
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
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}

	/**
	 * Parses a date string into a LocalDate object.
	 *
	 * @param dateStr the date string in yyyy-MM-dd format
	 * @return the parsed LocalDate object
	 * @throws DateTimeParseException if the date string is invalid
	 */
	private LocalDate parseDate(String dateStr) throws DateTimeParseException {
		return LocalDate.parse(dateStr);
	}
}