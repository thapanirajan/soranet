package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.soranet.model.PlanModel;
import com.soranet.model.UserModel;
import com.soranet.service.plan.PlanService;
import com.soranet.util.SessionUtil;

/**
 * Route: /admin/plans
 * Description: Handles admin CRUD operations on internet plans including
 *              creation, updating, searching, and conditional deletion.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/plans" })
public class PlanManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PlanService planService;

	/**
	 * Initializes the PlanService when the servlet is first loaded.
	 */
	@Override
	public void init() throws ServletException {
		planService = new PlanService();
	}

	/**
	 * Handles GET requests to /admin/plans.
	 * If user is admin, fetches all plans or filters them by name/speed if a search query is provided.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request, response))
			return;

		try {
			List<PlanModel> plans;

			// Optional search filtering
			String searchQuery = request.getParameter("searchQuery");
			if (searchQuery != null && !searchQuery.trim().isEmpty()) {
				plans = planService.searchPlansByNameOrSpeed(searchQuery);
			} else {
				plans = planService.getAllPlans();
			}

			request.setAttribute("plans", plans);
			request.getRequestDispatcher("/WEB-INF/views/admin/planManagement.jsp").forward(request, response);
		} catch (Exception e) {
			handleError(request, response, "Error loading plans: " + e.getMessage());
		}
	}

	/**
	 * Handles POST requests to /admin/plans.
	 * Supports both create/update and delete operations via _method override.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request, response))
			return;

		try {
			String method = request.getParameter("_method");

			// Support method override for DELETE via POST
			if ("DELETE".equalsIgnoreCase(method)) {
				handleDelete(request, response);
			} else {
				handleCreateOrUpdate(request, response);
			}
		} catch (NumberFormatException e) {
			handleError(request, response, "Invalid number format in input");
		} catch (IllegalArgumentException e) {
			handleError(request, response, e.getMessage());
		} catch (Exception e) {
			handleError(request, response, "Error processing plan: " + e.getMessage());
		}
	}

	/**
	 * Handles plan deletion. Only deletes a plan if it's not in use.
	 */
	private void handleDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		String idStr = request.getParameter("planId");

		if (idStr == null || idStr.isEmpty()) {
			handleError(request, response, "Plan ID is missing");
			return;
		}

		int planId = Integer.parseInt(idStr);

		// Only delete if the plan is not linked to any subscriptions
		boolean success = planService.deletePlanIfNoUser(planId);
		if (success) {
			request.setAttribute("successMessage", "Plan deleted successfully");
		} else {
			request.setAttribute("errorMessage", "Failed to delete plan");
		}
		doGet(request, response);
	}

	/**
	 * Handles both creation and update of plans.
	 * Validates all inputs, constructs PlanModel, and delegates to service.
	 */
	private void handleCreateOrUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		int planId = parseIntOrDefault(request.getParameter("planId"), 0);
		String planName = request.getParameter("planName");
		String speed = request.getParameter("speed");
		String planDuration = request.getParameter("planDuration");
		String planDescription = request.getParameter("planDescription");
		String type = request.getParameter("type");
		String priceStr = request.getParameter("price");
		String featuresStr = request.getParameter("features");
		boolean popular = "true".equalsIgnoreCase(request.getParameter("popular"));

		// Input validation
		if (isEmpty(planName, speed, planDuration, type) || priceStr == null) {
			throw new IllegalArgumentException("Required fields are missing");
		}

		double price = Double.parseDouble(priceStr);
		if (price < 0)
			throw new IllegalArgumentException("Price cannot be negative");
		if (!type.equalsIgnoreCase("residential") && !type.equalsIgnoreCase("business")) {
			throw new IllegalArgumentException("Invalid plan type");
		}

		// Parse features into a list
		List<String> features = featuresStr != null && !featuresStr.isEmpty()
				? Arrays.asList(featuresStr.split("\\s*,\\s*"))
				: List.of();

		// Construct model and persist
		PlanModel plan = new PlanModel(planId, planName, speed, price, planDuration, planDescription, type, popular,
				features, LocalDateTime.now());

		boolean success = (planId == 0) ? planService.createPlan(plan) : planService.updatePlan(plan);

		request.setAttribute("successMessage",
				success ? (planId == 0 ? "Plan created successfully" : "Plan updated successfully")
						: "Failed to " + (planId == 0 ? "create" : "update") + " plan");

		doGet(request, response);
	}

	/**
	 * Verifies if the current session user is an admin.
	 * Sends 403 if unauthorized.
	 */
	private boolean isAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return false;
		}
		return true;
	}

	/**
	 * Forwards to the error page with a specific error message.
	 */
	private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("errorMessage", message);
		request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
	}

	/**
	 * Utility method to check if any provided string is null or empty.
	 */
	private boolean isEmpty(String... values) {
		for (String v : values) {
			if (v == null || v.trim().isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * Parses an integer from string or returns a default value.
	 */
	private int parseIntOrDefault(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
