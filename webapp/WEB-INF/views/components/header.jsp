<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
* {
	padding: 0;
	margin: 0;
	box-sizing: border-box;
	font-family: Arial, Helvetica, sans-serif;
	line-height: 1.5;
}

header {
	padding: 1.2rem;
	border: 1px solid;
}

.header-content {
	display: flex;
	align-items: center;
	justify-content: space-around;
}

.logo {
	font-size: 1.25rem;
	display: flex;
	align-items: center;
	color: #003cff;
	gap: 0.5rem;
}

.wifi-logo {
	font-size: 2rem;
}

.logo-text {
	font-size: 1.2rem;
	font-weight: bold;
}

.header-content a {
	text-decoration: none;
	color: black;
}

.header-content nav {
	color: black;
	display: flex;
	align-items: center;
	gap: 1.2rem;
}

.nav-link {
	text-decoration: none;
	color: #374151;
	font-weight: 500;
	transition: color 0.3s;
}

.nav-link:hover {
	color: #2563eb;
}

.auth-links {
	display: flex;
	gap: 1rem;
	align-items: center;
}

.auth-login {
	text-decoration: none;
	color: #2563eb;
	font-weight: 600;
	transition: color 0.3s;
}

.auth-login:hover {
	color: #1d4ed8;
}

.auth-signup {
	background-color: #2563eb;
	color: white;
	padding: 0.5rem 1rem;
	border-radius: 0.375rem;
	text-decoration: none;
	font-weight: 500;
	transition: background-color 0.3s;
}

.auth-signup:hover {
	background-color: #1d4ed8;
}

.user-menu {
	position: relative;
	cursor: pointer;
}

.username {
	color: #475569;
	font-weight: 500;
}

.username:hover {
	color: #1e293b;
}

.dropdown {
	display: none;
	position: absolute;
	top: 100%;
	right: 0;
	background-color: white;
	border: 1px solid #e2e8f0;
	border-radius: 0.25rem;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
	min-width: 120px;
	z-index: 10;
}

.user-menu:hover .dropdown {
	display: block;
}

.dropdown a {
	display: block;
	padding: 0.5rem 1rem;
	color: #475569;
	text-decoration: none;
	font-weight: 500;
}

.dropdown a:hover {
	background-color: #f1f5f9;
	color: #1e293b;
}
</style>


<header class="header">
	<div class="header-content">

		<a href="${pageContext.request.contextPath}/" class="logo"> <span
			class="wifi-logo">📶</span> <span class="logo-text">SoraNet</span>
		</a>

		<nav class="nav">
			<a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
			<a href="${pageContext.request.contextPath}/plan" class="nav-link">Plans
				and Pricing</a> <a href="${pageContext.request.contextPath}/about"
				class="nav-link">About Us</a> <a
				href="${pageContext.request.contextPath}/contact" class="nav-link">Contact</a>
		</nav>


		<div class="auth-links">
			<c:choose>
				<c:when test="${not empty sessionScope.user}">
					<div class="user-menu">
						<span>${sessionScope.user.getUsername()}</span>
						<div class="dropdown">
							<a href="${pageContext.request.contextPath}/user/profile">Profile</a>
							<a href="${pageContext.request.contextPath}/logout">Logout</a>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="auth-links">
						<a href="${pageContext.request.contextPath}/login"
							class="auth-login">Login</a> <a style="text-decoration: none; color:white;" 
							href="${pageContext.request.contextPath}/register"
							class="auth-signup">Register</a>
					</div>
				</c:otherwise>
			</c:choose>
		</div>

	</div>
</header>


