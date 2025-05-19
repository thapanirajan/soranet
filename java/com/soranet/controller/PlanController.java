package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.soranet.model.PlanModel;
import com.soranet.service.plan.PlanService;

/**
 * Servlet for handling plan-related requests. Handles GET requests to display
 * available plans and POST requests by delegating to GET handling.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/plan" })
public class PlanController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public PlanController() {
		super();
	}

	/**
	 * Handles GET requests to display the plans page. Retrieves residential and
	 * business plans and forwards to the plans JSP.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Initialize plan service
			PlanService planService = new PlanService();
			// Fetch residential and business plans
			List<PlanModel> residentialPlans = planService.getPlansByType("residential");
			List<PlanModel> businessPlans = planService.getPlansByType("business");

			// Set plans for JSP
			request.setAttribute("residentialPlans", residentialPlans);
			request.setAttribute("businessPlans", businessPlans);

			// Forward to plans page
			request.getRequestDispatcher("WEB-INF/views/customer/plans.jsp").forward(request, response);
		} catch (Exception e) {
			// Handle errors and forward to error page
			request.setAttribute("error", "Error loading plans: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/error.jsp").forward(request, response);
		}
	}

	/**
	 * Handles POST requests by delegating to doGet. No additional processing is
	 * performed for POST requests.
	 *
	 * @param request  the HttpServletRequest object containing form data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Delegate to doGet for consistent behavior
		doGet(request, response);
	}
}