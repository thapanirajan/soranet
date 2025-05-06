package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.soranet.model.PlanModel;
import com.soranet.service.CustomerPageService;

/**
 * Servlet implementation class PlanController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/plan" })
public class PlanController extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		List<PlanModel> residentialPlans = CustomerPageService.getPlansByType("residential");
		List<PlanModel> businessPlans = CustomerPageService.getPlansByType("business");
		
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
