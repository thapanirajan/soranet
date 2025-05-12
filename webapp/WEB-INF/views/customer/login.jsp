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
<style>
@charset "UTF-8";
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	background-color: #ffffff;
}

.header {
	display: flex;
	margin: 0 10rem;
	border-bottom: 1px solid rgba(0, 0, 0, 0.4);
	justify-content: space-between;
	align-items: center;
	padding: 2rem 4rem;
}

.logo {
	font-size: 1.5rem;
	font-weight: bold;
	color: #064e3b;
}

.signup-link {
	font-size: 0.875rem;
	color: #059669;
}

.container {
	min-height: calc(100vh - 200px);
	display: flex;
	justify-content: center;
	align-items: center;
}

.login-box {
	border: 1px solid #000000;
	border-top: 1px solid #059669;
	padding: 5rem;
	box-shadow: 0 20px 25px -5px rgb(0 0 0/ 0.1), 0 8px 10px -6px
		rgb(0 0 0/ 0.1);
	border-radius: 0.8rem;
	background-color: white;
	border: 1px solid #059669;
	width: 40rem;
}

.login-title {
	margin-bottom: 2rem;
	font-size: 1.875rem;
	color: #064e3b;
	font-weight: bold;
	text-align: center;
}

.login-form {
	display: flex;
	flex-direction: column;
	gap: 1.5rem;
	width: 100%;
}

.input-field {
	width: 100%;
	padding: 0.5rem 0;
	border: none;
	outline: none;
}

.underline {
	height: 1px;
	background-color: #475569;
	width: 100%;
}

.error-message {
	color: #ef4444;
	font-size: 0.875rem;
	margin-top: 0.25rem;
}

.login-button {
	padding: 0.5rem 1.5rem;
	background-color: #059669;
	color: white;
	font-weight: bold;
	border: 1px solid #1e293b;
	border-radius: 0.25rem;
	cursor: pointer;
	outline: none;
}

.button-group {
	display: flex;
	justify-content: center;
}

.register-link {
	text-align: center;
}

.footer {
	margin: 0 10rem;
	text-align: center;
	padding: 1rem;
	font-size: 0.875rem;
	color: #04070a;
	background-color: white;
	border-top: 1px solid rgba(0, 0, 0, 0.5);
}

small {
	font-size: 14px;
} 

</style>
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
					<label> <input type="checkbox" name="rememberMe">
						Remember Me
					</label>
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