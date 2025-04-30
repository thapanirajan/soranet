/*
 * package com.soranet.filter;
 * 
 * import jakarta.servlet.*; import jakarta.servlet.http.HttpServletResponse;
 * import jakarta.servlet.annotation.WebFilter; import
 * jakarta.servlet.http.Cookie; import jakarta.servlet.http.HttpServletRequest;
 * import com.soranet.model.UserModel; import com.soranet.service.AuthService;
 * import com.soranet.util.CookieUtil; import com.soranet.util.SessionUtil;
 * import java.io.IOException;
 * 
 * @WebFilter("/*") public class AutoLoginFilter implements Filter {
 * 
 * public void doFilter(ServletRequest request, ServletResponse response,
 * FilterChain chain) throws IOException, ServletException { HttpServletRequest
 * httpRequest = (HttpServletRequest) request; HttpServletResponse httpResponse
 * = (HttpServletResponse) response;
 * 
 * // Check if user is not logged in if (SessionUtil.getAttribute(httpRequest,
 * "user") == null) { Cookie tokenCookie = CookieUtil.getCookie(httpRequest,
 * "rememberToken");
 * 
 * if (tokenCookie != null) { String token = tokenCookie.getValue(); UserModel
 * user = AuthService.validateToken(token);
 * 
 * if (user != null) { SessionUtil.setAttribute(httpRequest, "user", user);
 * 
 * // Redirect user based on role after successful auto-login String
 * redirectPath = switch (user.getRole().toLowerCase()) { case "admin" ->
 * "/admin/dashboard"; case "user" -> "/"; default -> "/login"; };
 * 
 * httpResponse.sendRedirect(httpRequest.getContextPath() + redirectPath);
 * return; } } }
 * 
 * chain.doFilter(request, response); } }
 */