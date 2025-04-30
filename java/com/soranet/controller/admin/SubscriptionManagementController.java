package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");

			// Validate inputs
			if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
				throw new IllegalArgumentException("Date fields are required");
			}
			if (userId <= 0 || planId <= 0) {
				throw new IllegalArgumentException("Invalid user ID or plan ID");
			}

			SubscriptionModel subscription = new SubscriptionModel(subscriptionId, userId, planId, startDate, endDate);

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
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error updating subscription: " + e.getMessage());
			doGet(request, response);
		}
	}

	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}
}