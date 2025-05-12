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

@WebServlet(asyncSupported = true, urlPatterns = { "/admin/plans" })
public class PlanManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PlanService planService;
	@Override
	public void init() throws ServletException {
		planService = new PlanService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request, response))
			return;

		try {
			List<PlanModel> plans = planService.getAllPlans();
			request.setAttribute("plans", plans);
			request.getRequestDispatcher("/WEB-INF/views/admin/planManagement.jsp").forward(request, response);
		} catch (Exception e) {
			handleError(request, response, "Error loading plans: " + e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request, response))
			return;

		try {
			String method = request.getParameter("_method");

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

	private void handleDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		String idStr = request.getParameter("planId");
		if (idStr == null || idStr.isEmpty()) {
			handleError(request, response, "Plan ID is missing");
			return;
		}

		int planId = Integer.parseInt(idStr);
		boolean success = planService.deletePlanIfNoUser(planId);
		if (success) {
			request.setAttribute("successMessage", "Plan deleted successfully");
		} else {
			request.setAttribute("errorMessage", "Failed to delete plan");
		}
		doGet(request, response);
	}

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

		if (isEmpty(planName, speed, planDuration, type) || priceStr == null) {
			throw new IllegalArgumentException("Required fields are missing");
		}

		double price = Double.parseDouble(priceStr);
		if (price < 0)
			throw new IllegalArgumentException("Price cannot be negative");
		if (!type.equalsIgnoreCase("residential") && !type.equalsIgnoreCase("business")) {
			throw new IllegalArgumentException("Invalid plan type");
		}

		List<String> features = featuresStr != null && !featuresStr.isEmpty()
				? Arrays.asList(featuresStr.split("\\s*,\\s*"))
				: List.of();

		PlanModel plan = new PlanModel(planId, planName, speed, price, planDuration, planDescription, type, popular,
				features, LocalDateTime.now());

		boolean success = (planId == 0) ? planService.createPlan(plan): planService.updatePlan(plan);

		request.setAttribute("successMessage",
				success ? (planId == 0 ? "Plan created successfully" : "Plan updated successfully")
						: "Failed to " + (planId == 0 ? "create" : "update") + " plan");

		doGet(request, response);
	}

	private boolean isAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return false;
		}
		return true;
	}

	private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("errorMessage", message);
		request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
	}

	private boolean isEmpty(String... values) {
		for (String v : values) {
			if (v == null || v.trim().isEmpty())
				return true;
		}
		return false;
	}

	private int parseIntOrDefault(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
