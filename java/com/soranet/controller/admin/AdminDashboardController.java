package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.soranet.service.dashboard.DashboardService;

/**
 * Route: /admin/dashboard Description: Handles HTTP requests for the Admin
 * Dashboard. Retrieves dashboard statistics and displays them on the dashboard
 * view.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/dashboard" })
public class AdminDashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Service layer instance to interact with dashboard-related data
	DashboardService dashboardService = new DashboardService();

	public AdminDashboardController() {
		super();
	}

	/**
	 * Handles HTTP GET requests to the /admin/dashboard endpoint. Fetches the
	 * dashboard statistics via DashboardService and forwards the data to the JSP
	 * view.
	 *
	 * @param request  The HttpServletRequest object.
	 * @param response The HttpServletResponse object.
	 * @throws ServletException if a servlet-specific error occurs.
	 * @throws IOException      if an I/O error occurs.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Fetch dashboard statistics (e.g., total users, subscriptions, revenue, etc.)
			var dashboardData = dashboardService.getDashboardData();

			// Log retrieved data for debugging (optional)
			System.out.println("dashboardData: " + dashboardData);

			// Attach dashboard data to the request scope
			request.setAttribute("dashboardData", dashboardData);

			// Forward request to dashboard JSP for rendering
			request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);

		} catch (ClassNotFoundException | SQLException e) {
			// Handle and log potential database or class loading errors
			e.printStackTrace();
		}
	}

	/**
	 * Handles HTTP POST requests by delegating to the doGet method. Useful when the
	 * same data is needed for both GET and POST (e.g., refresh).
	 *
	 * @param request  The HttpServletRequest object.
	 * @param response The HttpServletResponse object.
	 * @throws ServletException if a servlet-specific error occurs.
	 * @throws IOException      if an I/O error occurs.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
