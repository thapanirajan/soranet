package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.soranet.model.PlanModel;
import com.soranet.model.UserModel;
import com.soranet.service.AdminService;
import com.soranet.util.SessionUtil;

/**
 * Servlet implementation class PlanManagementController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/plans" })
public class PlanManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminService adminService;

	@Override
	public void init() throws ServletException {
		adminService = new AdminService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlanManagementController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			List<PlanModel> plans = adminService.getAllPlans();
			request.setAttribute("plans", plans);
			request.getRequestDispatcher("/WEB-INF/views/admin/planManagement.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error loading plans: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			int planId = Integer.parseInt(request.getParameter("planId"));
			String planName = request.getParameter("planName");
			String speed = request.getParameter("speed");
			double price = Double.parseDouble(request.getParameter("price"));
			String planDuration = request.getParameter("planDuration");
			String planDescription = request.getParameter("planDescription");
			String type = request.getParameter("type");
			String featuresStr = request.getParameter("features");
			List<String> features = featuresStr != null && !featuresStr.isEmpty()
					? Arrays.asList(featuresStr.split("\\s*,\\s*"))
					: Arrays.asList();
			boolean popular = "true".equalsIgnoreCase(request.getParameter("popular"));

			// Validate inputs
			if (planName == null || planName.isEmpty() || speed == null || speed.isEmpty() || planDuration == null
					|| planDuration.isEmpty() || type == null || type.isEmpty()) {
				throw new IllegalArgumentException("Required fields are missing");
			}
			if (price < 0) {
				throw new IllegalArgumentException("Price cannot be negative");
			}
			if (!"residential".equalsIgnoreCase(type) && !"business".equalsIgnoreCase(type)) {
				throw new IllegalArgumentException("Invalid plan type");
			}

			/*
			 * public PlanModel(int planId, String planName, String speed, BigDecimal price,
			 * int planDuration, String planDescription, String type, boolean popular,
			 * String features, LocalDateTime createdAt) {
			 */
			PlanModel plan = new PlanModel(planId, planName, speed, price, planDuration,
			        planDescription, type, popular, features, LocalDateTime.now());
			
			boolean success;
			if (planId == 0) {
				success = adminService.createInternetPlan(plan);
				request.setAttribute("successMessage", success ? "Plan created successfully" : "Failed to create plan");
			} else {
				success = adminService.updateInternetPlan(plan);
				request.setAttribute("successMessage", success ? "Plan updated successfully" : "Failed to update plan");
			}

			doGet(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid number format in input");
			doGet(request, response);
		} catch (IllegalArgumentException e) {
			request.setAttribute("errorMessage", e.getMessage());
			doGet(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error processing plan: " + e.getMessage());
			doGet(request, response);
		}
	}

	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}
}
