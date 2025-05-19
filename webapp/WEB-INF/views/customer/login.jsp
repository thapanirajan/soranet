<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.soranet.model.UserModel"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
	<%
	UserModel user = (UserModel) request.getAttribute("user");
	String loginId = (user != null) ? user.getUsername() : "";
	%>
	<div class="header">
		<a class="logo" href="${pageContext.request.contextPath}/">SoraNet</a>
		<div class="signup-link">
			Don't have an account? <a
				href="${pageContext.request.contextPath}/register">Sign Up</a>
		</div>
	</div>
	<div class="container">
		<div class="login-box">
			<h1 class="login-title">Login</h1>
			<form action="${pageContext.request.contextPath}/login" method="POST"
				class="login-form" id="loginForm">
				<div class="input-group">
					<input type="text" name="loginId" id="email"
						placeholder="email or username" value="<%=loginId%>"
						class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="password" name="password" id="password"
						placeholder="Password" class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<label><input type="checkbox" name="rememberMe">
						Remember Me</label>
				</div>
				<div class="button-group">
					<input type="submit" value="Login" class="login-button">
				</div>
				<c:if test="${not empty message}">
					<p class="error-message">
						<c:out value="${message}" />
					</p>
				</c:if>
			</form>
		</div>
	</div>
	<div class="footer">@SoraNet 2025</div>
</body>
</html>
