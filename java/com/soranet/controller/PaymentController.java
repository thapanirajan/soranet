package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.soranet.model.PlanModel;
import com.soranet.service.CustomerPageService;
import com.soranet.util.ValidationUtil;

/**
 * Servlet implementation class PaymentController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/payment" })
public class PaymentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int planId = Integer.parseInt(request.getParameter("planId"));
		try {
			PlanModel selectedPlan = CustomerPageService.getPlanById(planId);

			request.setAttribute("selectedPlan", selectedPlan);
			request.getRequestDispatcher("/WEB-INF/views/customer/payment.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String paymentMethod = request.getParameter("paymentMethod");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String address = request.getParameter("address");

		String cardNumber = request.getParameter("cardNumber");
		String cvv = request.getParameter("cvv");
		String expiry = request.getParameter("expiry");

		String esewaId = request.getParameter("esewaId");
		String esewaPassword = request.getParameter("esewaPassword");

		String bankId = request.getParameter("bankId");
		String bankPassword = request.getParameter("bankPassword");
		
		
	}

}
