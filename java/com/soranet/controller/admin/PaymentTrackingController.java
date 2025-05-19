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
 * Servlet implementation class PaymentTrackingController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/admin/payments" })
public class PaymentTrackingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PaymentService paymentService;

	@Override
	public void init() throws ServletException {
		paymentService = new PaymentService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
			return;
		}

		try {
			List<PaymentModel> payments;
			String subscriptionIdParam = request.getParameter("subscriptionId");
			if (subscriptionIdParam != null && !subscriptionIdParam.trim().isEmpty()) {
				try {
					int subscriptionId = Integer.parseInt(subscriptionIdParam);
					payments = paymentService.getPaymentsBySubscriptionId(subscriptionId);
				} catch (NumberFormatException e) {
					request.setAttribute("errorMessage", "Invalid Subscription ID format");
					payments = paymentService.getAllPayments();
				}
			} else {
				payments = paymentService.getAllPayments();
			}
			request.setAttribute("payments", payments);
			request.getRequestDispatcher("/WEB-INF/views/admin/paymentTracking.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error loading payments: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/admin/error.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private boolean isAdmin(HttpServletRequest request) {
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		return user != null && "admin".equalsIgnoreCase(user.getRole());
	}
}