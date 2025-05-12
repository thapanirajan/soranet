
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

@WebFilter(urlPatterns = { "/admin/*", "/payment/*", "/user/profile" })
public class AuthFilter implements Filter {

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get user from session
        UserModel user = (UserModel) SessionUtil.getAttribute(httpRequest, "user");

        // Log user details for debugging
        String requestedUrl = httpRequest.getRequestURI();
        if (httpRequest.getQueryString() != null) {
            requestedUrl += "?" + httpRequest.getQueryString();
        }
        System.out.println("AuthFilter: Requested URL: " + requestedUrl + ", User: " + (user != null ? user.getUsername() + ", Role: " + user.getRole() : "null"));

        if (user == null) {
            // Store the requested URL for redirection after login
            SessionUtil.setAttribute(httpRequest, "redirectUrl", requestedUrl);
            // Redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Check if user is admin for /admin/* paths
        if (requestedUrl.startsWith(httpRequest.getContextPath() + "/admin/") && !"admin".equalsIgnoreCase(user.getRole())) {
            System.out.println("AuthFilter: User " + user.getUsername() + " is not an admin, redirecting to home");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
            return;
        }

        // User is authenticated and authorized, proceed with request
        chain.doFilter(request, response);
    }
}
