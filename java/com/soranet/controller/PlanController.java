package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.soranet.model.InternetPlanModel;
import com.soranet.service.CustomerPageService;

/**
 * Servlet implementation class PlanController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/plan" })
public class PlanController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CustomerPageService planService;

	@Override
	public void init() throws ServletException {
		planService = new CustomerPageService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlanController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<InternetPlanModel> residentialPlans = planService.getPlansByType("residential");
		List<InternetPlanModel> businessPlans = planService.getPlansByType("business");
		
		request.setAttribute("residentialPlans", residentialPlans);
        request.setAttribute("businessPlans", businessPlans);
        
		request.getRequestDispatcher("WEB-INF/views/customer/plans.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
