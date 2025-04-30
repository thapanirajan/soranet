package com.soranet.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.soranet.util.SessionUtil;
import com.soranet.model.UserModel;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		UserModel user = (UserModel) SessionUtil.getAttribute(httpRequest, "user");
		if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
			return;
		}
		chain.doFilter(request, response);
	}
}