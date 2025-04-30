<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	background-color: #f1f5f9;
}

.header {
	display: flex;
	justify-content: space-around;
	align-items: center;
	padding: 1rem 2rem;
	background-color: white;
	border-bottom: 1px solid #e2e8f0;
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
	min-height: calc(100vh - 120px);
	display: flex;
	justify-content: center;
	align-items: center;
}

.register-box {
	padding: 3.5rem;
	box-shadow: 0 20px 25px -5px rgb(0 0 0/ 0.1), 0 8px 10px -6px
		rgb(0 0 0/ 0.1);
	border-radius: 0.5rem;
	background-color: white;
	width: 40rem;
}

.register-title {
	margin-bottom: 2rem;
	font-size: 1.875rem;
	color: #064e3b;
	font-weight: bold;
	text-align: center;
}

.register-form {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 1.5rem;
	width: 100%;
}

.input-group {
	position: relative;
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
	grid-column: span 2;
	text-align: center;
}

.register-button {
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
	grid-column: span 2;
	display: flex;
	justify-content: center;
}

.login-link {
	grid-column: span 2;
	text-align: center;
}

.footer {
	text-align: center;
	padding: 1rem;
	font-size: 0.875rem;
	color: #475569;
	background-color: white;
	border-top: 1px solid #e2e8f0;
}

small {
	font-size: 14px;
}

select.input-field {
	padding: 0.5rem;
	border: 1px solid #475569;
	border-radius: 0.25rem;
	width: 100%;
}

input[type="file"] {
	padding: 0.5rem 0;
	width: 100%;
}
</style>
</head>
<body>
	<div class="header">
		<a class="logo" href="${pageContext.request.contextPath}/ ">SoraNet</a>
		<div class="signup-link">
			Already have an account? <a
				href="${pageContext.request.contextPath}/login">Login</a>
		</div>
	</div>
	<div class="container">
		<div class="register-box">
			<h1 class="register-title">Register</h1>
			<form action="${pageContext.request.contextPath}/register"
				method="POST" class="register-form" id="registerForm"
				enctype="multipart/form-data">
				<div class="input-group">
					<input type="text" name="firstName" id="firstName"
						placeholder="First Name" class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="text" name="lastName" id="lastName"
						placeholder="Last Name" class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="email" name="email" id="email" placeholder="Email"
						class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="tel" name="phoneNumber" id="phoneNumber"
						placeholder="Phone Number" class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<select name="role" id="role" class="input-field" required>
						<option value="" disabled selected>Select Role</option>
						<option value="Customer">Customer</option>
						<option value="Admin">Admin</option>
					</select>
				</div>
				<div class="input-group">
					<input type="text" name="username" id="username"
						placeholder="Username" class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="text" name="address" id="address"
						placeholder="Address" class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="text" name="city" id="city" placeholder="City"
						class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="password" name="password" id="password"
						placeholder="Password" class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="password" name="confirm_password"
						id="confirm_password" placeholder="Confirm Password"
						class="input-field" required>
					<div class="underline"></div>
				</div>
				<div class="input-group">
					<input type="file" accept="image/*" name="profilePicture" id="profilePicture"
						class="input-field">
					<div class="underline"></div>
				</div>
				<div class="button-group">
					<input type="submit" value="Register" class="register-button">
				</div>

				<%-- Check if 'message' is not empty --%>
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