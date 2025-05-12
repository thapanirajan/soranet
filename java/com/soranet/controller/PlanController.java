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

@WebServlet(asyncSupported = true, urlPatterns = { "/plan" })
public class PlanController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public PlanController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PlanService planService = new PlanService();
            List<PlanModel> residentialPlans = planService.getPlansByType("residential");
            List<PlanModel> businessPlans = planService.getPlansByType("business");

            request.setAttribute("residentialPlans", residentialPlans);
            request.setAttribute("businessPlans", businessPlans);

            request.getRequestDispatcher("WEB-INF/views/customer/plans.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading plans: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/customer/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}