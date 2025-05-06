package com.soranet.filter;

import java.io.IOException;

import com.soranet.model.UserModel;
import com.soranet.util.SessionUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter({ "/login" })
public class RoleRedirectFilter implements jakarta.servlet.Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		UserModel user = (UserModel) SessionUtil.getAttribute(httpRequest, "user");

		if (user != null) {
			String redirectPath = determineRedirectPath(user.getRole());
			System.out.print(user.getRole());
			httpResponse.sendRedirect(httpRequest.getContextPath() + redirectPath);
			return;
		}

		chain.doFilter(request, response);
	}

	// determine redirect path according to the role of the user
	private String determineRedirectPath(String role) {
		if (role == null) {
			throw new IllegalArgumentException("Role cannot be null");
		}
		return switch (role.toLowerCase()) {
		case "admin" -> "/admin/dashboard";
		case "customer" -> "/";
		default -> throw new IllegalArgumentException("Unknown role:  " + role.toLowerCase());
		};
	}
}
