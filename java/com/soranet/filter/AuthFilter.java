//package com.soranet.filter;
//
//import java.io.IOException;
//
//import com.soranet.model.UserModel;
//import com.soranet.util.SessionUtil;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebFilter(urlPatterns = { "/admin/*", "/payment/*" })
//public class AuthFilter {
//	private static final String Login_page = "/WEB-INF/views/customer/login.jsp";
//
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//		// Get user from session
//		UserModel user = (UserModel) SessionUtil.getAttribute(httpRequest, "user");
//
//		if (user == null) {
//			// Redirect to login page
//			httpResponse.sendRedirect(httpRequest.getContextPath() + Login_page);
//			return;
//		}
//		// User is authenticated, proceed with request
//		chain.doFilter(request, response);
//	}
//
//}

package com.soranet.filter;

import com.soranet.model.UserModel;
import com.soranet.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = { "/admin/*", "/payment/*","/user/profile" })
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Get user from session
		UserModel user = (UserModel) SessionUtil.getAttribute(httpRequest, "user");

		if (user == null) {
			String requestedUrl = httpRequest.getRequestURI();
			if (httpRequest.getQueryString() != null) {
				requestedUrl += "?" + httpRequest.getQueryString();
			}
			SessionUtil.setAttribute(httpRequest, "redirectUrl", requestedUrl);

			// Redirect to login page
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
			return;
		}

		// User is authenticated, proceed with request
		chain.doFilter(request, response);
	}
}
