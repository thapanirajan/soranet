<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<style>
@charset "UTF-8";

* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
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
	font-size: 2.5rem;
	font-weight: bold;
	color: #080c18;
}

.signup-link {
	font-size: 0.875rem;
	color: #059669;
}

.container {
	min-height: calc(100vh - 120px);
	display: flex;
	justify-content: space-around;
	align-items: center;
}

.register-box {
	border: 2px solid #080c18;
	padding: 3.5rem 4rem;
	box-shadow: 0 20px 25px -5px rgb(0 0 0/ 0.1), 0 8px 10px -6px
		rgb(0 0 0/ 0.1);
	border-radius: 0.5rem;
	background-color: white;
	width: 50rem;
}

.register-title {
	margin-bottom: 2rem;
	font-size: 3rem;
	color: #080c18;
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
	padding: 1rem 0;
	font-size: 1.2rem;
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
	width: 100%;
	padding: 1rem 0;
	margin-top: 1rem;
	background-color: #080c18;
	color: white;
	font-size: 1rem;
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

.footer {
	margin: 0 10rem;
	text-align: center;
	padding: 1rem;
	font-size: 0.875rem;
	color: #04070a;
	background-color: white;
	border-top: 1px solid rgba(0, 0, 0, 0.5);
}

input[type="file"] {
	padding: 0.8rem 0;
	width: 100%;
}

</style>
</head>
<body>
	<div class="header">
		<a class="logo" href="${pageContext.request.contextPath}/">SoraNet</a>
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
					<input type="file" accept="image/*" name="profilePicture"
						id="profilePicture" class="input-field">
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
				<div class="button-group">
					<input type="submit" value="Register" class="register-button"
						id="registerButton">
				</div>
				<p id="clientError" class="error-message" style="display: none;"></p>
				<c:if test="${not empty errorMessage}">
					<p class="error-message">
						<c:out value="${errorMessage}" />
					</p>
				</c:if>
			</form>
		</div>
	</div>
	<div class="footer">@SoraNet 2025</div>

	<script>
		document
				.addEventListener(
						"DOMContentLoaded",
						function() {
							const form = document
									.getElementById("registerForm");
							const password = document
									.getElementById("password");
							const confirmPassword = document
									.getElementById("confirm_password");
							const clientError = document
									.getElementById("clientError");
							const registerButton = document
									.getElementById("registerButton");

							form
									.addEventListener(
											"submit",
											function(e) {
												clientError.style.display = "none";

												if (password.value !== confirmPassword.value) {
													e.preventDefault();
													clientError.textContent = "Passwords do not match.";
													clientError.style.display = "block";
													return;
												}
											});

							confirmPassword
									.addEventListener(
											"input",
											function() {
												if (password.value !== confirmPassword.value) {
													confirmPassword.style.borderBottom = "2px solid #ef4444";
												} else {
													confirmPassword.style.borderBottom = "2px solid #059669";
												}
											});
						});
	</script>
</body>
</html>
