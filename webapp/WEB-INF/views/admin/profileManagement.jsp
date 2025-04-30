<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Profile Management</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: Arial, sans-serif;
	background-color: #f8f9fa;
}

.container-dash {
	display: flex;
	min-height: 100vh;
}

.dashboard-container {
	flex-grow: 1;
	padding: 2rem;
}

h2 {
	margin-bottom: 1.5rem;
	color: #1f2937;
}

.alert {
	padding: 1rem;
	margin-bottom: 1.5rem;
	border-radius: 4px;
	position: relative;
	color: #fff;
}

.alert-success {
	background-color: #2ecc71;
}

.alert-error {
	background-color: #e74c3c;
}

.alert-close {
	position: absolute;
	right: 1rem;
	top: 1rem;
	background: none;
	border: none;
	color: #fff;
	font-size: 1rem;
	cursor: pointer;
}

.profile-form {
	background-color: #fff;
	padding: 2rem;
	border-radius: 8px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
	max-width: 600px;
}

.profile-form label {
	display: block;
	margin-bottom: 0.5rem;
	font-weight: bold;
	color: #1f2937;
}

.profile-form input, .profile-form select {
	width: 100%;
	padding: 0.75rem;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-bottom: 1.5rem;
	font-size: 1rem;
}

.profile-form input[readonly] {
	background-color: #e5e7eb;
}

.profile-form button {
	padding: 0.75rem 1.5rem;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1rem;
}

.profile-form .btn-primary {
	background-color: #1f2937;
	color: #fff;
}

.profile-form .btn-primary:hover {
	background-color: #374151;
}

@media ( max-width : 768px) {
	.dashboard-container {
		padding: 1rem;
	}
	.profile-form {
		padding: 1rem;
	}
	.profile-form input, .profile-form select {
		font-size: 0.9rem;
	}
	.profile-form button {
		padding: 0.5rem 1rem;
		font-size: 0.9rem;
	}
}
</style>
</head>
<body>
	<div class="container-dash">
		<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
		<div class="dashboard-container">
			<h2>Profile Management</h2>

			<c:if test="${not empty successMessage}">
				<div class="alert alert-success">
					${successMessage}
					<button class="alert-close"
						onclick="this.parentElement.style.display='none'">×</button>
				</div>
			</c:if>
			<c:if test="${not empty errorMessage}">
				<div class="alert alert-error">
					${errorMessage}
					<button class="alert-close"
						onclick="this.parentElement.style.display='none'">×</button>
				</div>
			</c:if>

			<form class="profile-form"
				action="${pageContext.request.contextPath}/admin/profile"
				method="post">
				<div>
					<label for="username">Username</label> <input type="text"
						id="username" value="${user.username}" readonly>
				</div>
				<div>
					<label for="firstName">First Name</label> <input type="text"
						name="firstName" id="firstName" value="${user.firstName}" required>
				</div>
				<div>
					<label for="lastName">Last Name</label> <input type="text"
						name="lastName" id="lastName" value="${user.lastName}" required>
				</div>
				<div>
					<label for="email">Email</label> <input type="email" name="email"
						id="email" value="${user.email}" required>
				</div>
				<div>
					<label for="phoneNumber">Phone Number</label> <input type="tel"
						name="phoneNumber" id="phoneNumber" value="${user.phoneNumber}">
				</div>
				<div>
					<label for="address">Address</label> <input type="text"
						name="address" id="address" value="${user.address}">
				</div>
				<div>
					<label for="city">City</label> <input type="text" name="city"
						id="city" value="${user.city}">
				</div>
				<button type="submit" class="btn-primary">Update Profile</button>
			</form>
		</div>
	</div>
</body>
</html>