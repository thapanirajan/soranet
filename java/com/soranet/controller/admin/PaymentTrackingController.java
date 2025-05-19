package com.soranet.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.soranet.model.PaymentModel;
import com.soranet.model.UserModel;
import com.soranet.service.payment.PaymentService;
import com.soranet.util.SessionUtil;

/**
 * Route: /admin/payments
 * Description: Handles admin-side payment tracking.
 *              Displays all payment records, or filters them by Subscription ID if provided.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/payments" })
public class PaymentTrackingController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PaymentService paymentService;

	/**
	 * Initializes the PaymentService when the servlet is first loaded.
	 *
	 * @throws ServletException if initialization fails.
	 */
	@Override
	public void init() throws ServletException {
		paymentService = new PaymentService();
	}

	/**
	 * Handles HTTP GET requests to /admin/payments.
	 * If the user is an admin, fetches payment data — optionally filtered by subscription ID —
	 * and forwards it to the JSP view. If access is denied or an error occurs, handles it accordingly.
	 *
	 * @param request  the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-specific error occurs.
	 * @throws IOException      if an I/O error occurs.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Access control: only allow admins
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			List<PaymentModel> payments;

			// Check if a subscription ID is provided for filtering
			String subscriptionIdParam = request.getParameter("subscriptionId");
			if (subscriptionIdParam != null && !subscriptionIdParam.trim().isEmpty()) {
				try {
					int subscriptionId = Integer.parseInt(subscriptionIdParam);

					// Fetch payments for a specific subscription
					payments = paymentService.getPaymentsBySubscriptionId(subscriptionId);
				} catch (NumberFormatException e) {
					// If ID is invalid, show all and set an error message
					request.setAttribute("errorMessage", "Invalid Subscription ID format");
					payments = paymentService.getAllPayments();
				}
			} else {
				// No filter — fetch all payments
				payments = paymentService.getAllPayments();
			}

			// Pass payment data to the JSP
			request.setAttribute("payments", payments);
			request.getRequestDispatcher("/WEB-INF/views/admin/paymentTracking.jsp").forward(request, response);
		} catch (Exception e) {
			// Handle unexpected errors and forward to error page
			request.setAttribute("errorMessage", "Error loading payments: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
		}
	}

	/**
	 * Handles HTTP POST requests by delegating to doGet.
	 * Useful for filter submissions or refreshing the page via POST.
	 *
	 * @param request  the HttpServletRequest object.
	 * @param response the HttpServletResponse object.
	 * @throws ServletException if a servlet-specific error occurs.
	 * @throws IOException      if an I/O error occurs.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Utility method to check if the current session user is an admin.
	 *
	 * @param request the HttpServletRequest object.
	 * @return true if the user is an admin; false otherwise.
	 */
	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}
}
