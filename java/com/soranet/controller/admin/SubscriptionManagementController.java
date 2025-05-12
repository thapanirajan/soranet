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
 * Servlet implementation class SubscriptionManagementController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/subscriptions" })
public class SubscriptionManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	SubscriptionService subscriptionService = new SubscriptionService();


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			List<SubscriptionModel> subscriptions = subscriptionService.getAllSubscriptions();
			request.setAttribute("subscriptions", subscriptions);
			request.getRequestDispatcher("/WEB-INF/views/admin/subscriptionManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error loading subscriptions: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/components/error.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			// Get parameters
			int userId = Integer.parseInt(request.getParameter("userId"));
			int planId = Integer.parseInt(request.getParameter("planId"));
			String startDateStr = request.getParameter("startDate");
			String endDateStr = request.getParameter("endDate");
			String paymentMethod = request.getParameter("paymentMethod");

			// Validate inputs
			if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()
					|| paymentMethod == null || paymentMethod.isEmpty()) {
				throw new IllegalArgumentException("Required fields are missing");
			}
			if (userId <= 0 || planId <= 0) {
				throw new IllegalArgumentException("Invalid user ID or plan ID");
			}

			// Check if userId exists
			if (!subscriptionService.userExists(userId)) {
				throw new IllegalArgumentException("User with ID " + userId + " does not exist");
			}

			// Check if planId exists
			if (!subscriptionService.planExists(planId)) {
				throw new IllegalArgumentException("Plan with ID " + planId + " does not exist");
			}

			LocalDate startDate = parseDate(startDateStr);
			LocalDate endDate = parseDate(endDateStr);

			SubscriptionModel subscription = new SubscriptionModel(0, userId, planId, startDate, endDate, null);

			
			boolean success = subscriptionService.createSubscriptionWithPayment(subscription, paymentMethod);
			request.setAttribute("successMessage",
					success ? "Subscription and payment created successfully" : "Failed to create subscription");

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

	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}

	// Helper method to parse date string to LocalDate
	private LocalDate parseDate(String dateStr) throws DateTimeParseException {
		return LocalDate.parse(dateStr);
	}
}
