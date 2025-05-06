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
import com.soranet.service.AdminService;
import com.soranet.util.SessionUtil;

/**
 * Servlet implementation class SubscriptionManagementController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/subscriptions" })
public class SubscriptionManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminService adminService;

	@Override
	public void init() throws ServletException {
		adminService = new AdminService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			List<SubscriptionModel> subscriptions = adminService.getAllSubscriptions();
			request.setAttribute("subscriptions", subscriptions);
			request.getRequestDispatcher("/WEB-INF/views/admin/subscriptionManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error loading subscriptions: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			int subscriptionId = Integer.parseInt(request.getParameter("subscriptionId"));
			int userId = Integer.parseInt(request.getParameter("userId"));
			int planId = Integer.parseInt(request.getParameter("planId"));
			String startDateStr = request.getParameter("startDate");
			String endDateStr = request.getParameter("endDate");

			// Validate inputs
			if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()) {
				throw new IllegalArgumentException("Date fields are required");
			}
			if (userId <= 0 || planId <= 0) {
				throw new IllegalArgumentException("Invalid user ID or plan ID");
			}

			// Parse startDate and endDate to LocalDate
			LocalDate startDate = parseDate(startDateStr);
			LocalDate endDate = parseDate(endDateStr);

			SubscriptionModel subscription = new SubscriptionModel(subscriptionId, userId, planId, startDate, endDate, null);

			boolean success = adminService.updateSubscription(subscription);
			request.setAttribute("successMessage",
					success ? "Subscription updated successfully" : "Failed to update subscription");

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
			request.setAttribute("errorMessage", "Error updating subscription: " + e.getMessage());
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
