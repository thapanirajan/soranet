package com.soranet.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.soranet.model.InternetPlanModel;
import com.soranet.service.CustomerPageService;

/**
 * Servlet implementation class PaymentController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/payment" })
public class PaymentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	CustomerPageService service = new CustomerPageService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int planId = Integer.parseInt(request.getParameter("planId"));
		try {
			InternetPlanModel selectedPlan = service.getPlanById(planId);
			
			request.setAttribute("selectedPlan", selectedPlan);
			request.getRequestDispatcher("/WEB-INF/views/customer/payment.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
