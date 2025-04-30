//package com.soranet.util;
//
//import java.io.IOException;
//
//import com.soranet.service.AuthService;
//import com.soranet.service.CustomerPageService;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public abstract class BaseUtil extends HttpServlet {
//    protected AuthService authService;
//    protected CustomerPageService customerService;
//    
//    @Override
//    public void init() throws ServletException {
//        authService = new AuthService();
//        customerService = new CustomerPageService();
//    }
//
//    protected void handleError(HttpServletRequest request, 
//                             HttpServletResponse response,
//                             String message, 
//                             String redirectPath) throws ServletException, IOException {
//        request.setAttribute("errorMessage", message);
//        request.getRequestDispatcher(redirectPath).forward(request, response);
//    }
//
//    protected boolean isAuthenticated(HttpServletRequest request) {
//		return SessionUtil.getAttribute(request, "user") != null;
//	}
//}