package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.soranet.model.PlanModel;
import com.soranet.model.PaymentModel;
import com.soranet.model.SubscriptionModel;
import com.soranet.model.UserModel;
import com.soranet.service.payment.PaymentService;
import com.soranet.service.plan.PlanService;
import com.soranet.service.subscription.SubscriptionService;
import com.soranet.util.SessionUtil;
import com.soranet.util.ValidationUtil;

/**
 * Servlet for handling payment processing. Handles GET requests to display the
 * payment page and POST requests to process payments and create subscriptions.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/payment" })
public class PaymentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Handles GET requests to display the payment page. Retrieves the selected plan
	 * based on the provided plan ID and forwards to the payment JSP.
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Processing GET request for payment page");
		try {
			// Parse plan ID from request
			int planId = Integer.parseInt(request.getParameter("planId"));
			System.out.println("Fetching plan with ID: " + planId);
			PlanService planService = new PlanService();
			// Retrieve plan details
			PlanModel selectedPlan = planService.getPlanById(planId);

			if (selectedPlan == null) {
				System.out.println("Plan not found for ID: " + planId);
				throw new IllegalArgumentException("Invalid plan selected.");
			}

			// Set plan for JSP
			request.setAttribute("selectedPlan", selectedPlan);
			request.getRequestDispatcher("/WEB-INF/views/customer/payment.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			System.out.println("Invalid plan ID format: " + e.getMessage());
			request.setAttribute("error", "Invalid plan ID.");
			request.getRequestDispatcher("/WEB-INF/views/customer/error.jsp").forward(request, response);
		} catch (Exception e) {
			System.out.println("Error loading payment page: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", "An error occurred while loading the payment page.");
			request.getRequestDispatcher("/WEB-INF/views/customer/error.jsp").forward(request, response);
		}
	}

	/**
	 * Handles POST requests to process payments and create subscriptions. Validates
	 * user input, processes payment based on the selected method, creates a
	 * subscription, and records the payment.
	 *
	 * @param request  the HttpServletRequest object containing form data
	 * @param response the HttpServletResponse object for sending the response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs during request processing
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Processing POST request for payment");
		// Verify user is logged in
		UserModel user = (UserModel) SessionUtil.getAttribute(request, "user");
		if (user == null) {
			System.out.println("User not logged in, redirecting to login");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		int userId = user.getUserId();
		System.out.println("User ID from session: " + userId);

		try {
			// Extract form parameters
			int planId = Integer.parseInt(request.getParameter("planId"));
			String amountStr = request.getParameter("amount");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String address = request.getParameter("address");
			String paymentMethod = request.getParameter("paymentMethod");

			String cardNumber = request.getParameter("cardNumber");
			String expiry = request.getParameter("expiry");
			String cvv = request.getParameter("cvv");
			String esewaId = request.getParameter("esewaId");
			String esewaPassword = request.getParameter("esewaPassword");
			String bankId = request.getParameter("bankId");
			String bankPassword = request.getParameter("bankPassword");

			System.out.println("Form parameters - planId: " + planId + ", amount: " + amountStr + ", paymentMethod: "
					+ paymentMethod);

			// Validate required fields
			if (planId <= 0 || ValidationUtil.isNullOrEmpty(amountStr) || ValidationUtil.isNullOrEmpty(name)
					|| ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(address)
					|| ValidationUtil.isNullOrEmpty(paymentMethod)) {
				System.out.println("Validation failed: Missing required fields");
				throw new IllegalArgumentException("All required fields must be provided.");
			}

			// Validate amount
			double amount;
			try {
				amount = Double.parseDouble(amountStr);
				if (amount <= 0) {
					System.out.println("Validation failed: Amount must be positive");
					throw new IllegalArgumentException("Amount must be positive.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Validation failed: Invalid amount format");
				throw new IllegalArgumentException("Invalid amount format.");
			}

			// Validate payment method-specific fields
			switch (paymentMethod) {
			case "credit":
				if (ValidationUtil.isNullOrEmpty(cardNumber) || ValidationUtil.isNullOrEmpty(expiry)
						|| ValidationUtil.isNullOrEmpty(cvv)) {
					System.out.println("Validation failed: Missing credit card fields");
					throw new IllegalArgumentException("All credit card fields are required.");
				}
				if (!cvv.matches("\\d{3,4}")) {
					System.out.println("Validation failed: Invalid CVV format");
					throw new IllegalArgumentException("Invalid CVV format.");
				}
				System.out.println("Mock credit card payment processing");
				break;
			case "esewa":
				if (ValidationUtil.isNullOrEmpty(esewaId) || ValidationUtil.isNullOrEmpty(esewaPassword)) {
					System.out.println("Validation failed: Missing eSewa fields");
					throw new IllegalArgumentException("eSewa ID and password are required.");
				}
				System.out.println("Mock eSewa payment processing");
				break;
			case "bank":
				if (ValidationUtil.isNullOrEmpty(bankId) || ValidationUtil.isNullOrEmpty(bankPassword)) {
					System.out.println("Validation failed: Missing bank fields");
					throw new IllegalArgumentException("Bank ID and password are required.");
				}
				System.out.println("Mock bank payment processing");
				break;
			default:
				System.out.println("Validation failed: Invalid payment method");
				throw new IllegalArgumentException("Invalid payment method selected.");
			}

			// Verify plan and amount
			PlanService planService = new PlanService();
			System.out.println("Fetching plan with ID: " + planId);
			PlanModel plan = planService.getPlanById(planId);
			if (plan == null || plan.getPrice() != amount) {
				System.out.println("Plan not found or amount mismatch for planId: " + planId);
				throw new IllegalArgumentException("Invalid plan or amount mismatch.");
			}

			// Create subscription
			LocalDate startDate = LocalDate.now();
			LocalDate endDate = startDate.plusMonths(1);
			SubscriptionModel subscription = new SubscriptionModel(0, userId, planId, startDate, endDate,
					LocalDateTime.now());
			System.out.println("Creating subscription for userId: " + userId + ", planId: " + planId);
			SubscriptionService subscriptionService = new SubscriptionService();
			int subscriptionId = subscriptionService.createSubscription(subscription);
			System.out.println("Subscription created with ID: " + subscriptionId);

			// Record payment
			PaymentModel payment = new PaymentModel(0, subscriptionId, amount, LocalDateTime.now(), paymentMethod);
			System.out.println("Creating payment for subscriptionId: " + subscriptionId + ", amount: " + amount);
			PaymentService paymentService = new PaymentService();
			paymentService.createPayment(payment);
			System.out.println("Payment created successfully");

			// Display success message
			request.setAttribute("message", "Payment successful! Subscription activated.");
			request.getRequestDispatcher("/WEB-INF/views/components/success.jsp").forward(request, response);

		} catch (IllegalArgumentException e) {
			System.out.println("Validation error: " + e.getMessage());
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/customer/payment.jsp").forward(request, response);
		} catch (Exception e) {
			System.out.println("Error processing payment: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", "An error occurred while processing the payment.");
			request.getRequestDispatcher("/WEB-INF/views/components/error.jsp").forward(request, response);
		}
	}
}