<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Profile Management</title>
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/profileManagement.css"> --%>
<style>
.container-dash {
	display: flex;
	min-height: 100vh;
}

.dashboard-container {
	flex-grow: 1;
	padding: 20px;
	background-color: #fff;
}

h2 {
	margin-bottom: 20px;
	font-size: 24px;
	color: #333;
}

.alert {
	padding: 15px;
	margin-bottom: 20px;
	border-radius: 4px;
	position: relative;
}

.alert-success {
	background-color: #d4edda;
	color: #155724;
}

.alert-error {
	background-color: #f8d7da;
	color: #721c24;
}

.alert-close {
	position: absolute;
	top: 10px;
	right: 10px;
	background: none;
	border: none;
	font-size: 20px;
	cursor: pointer;
}

.profile-form {
	max-width: 600px;
	margin: 0 auto;
	display: flex;
	flex-direction: column;
	gap: 15px;
}

.pp {
	text-align: center;
	margin-bottom: 20px;
}

.preview-image {
	display: block;
	margin: 0 auto;
	width: 150px;
	height: 150px;
	border-radius: 50%;
	object-fit: cover;
	border: 2px solid #ced4da;
}

.change-pic-container {
	text-align: center;
	margin-bottom: 20px;
}

.change-picture-btn {
	display: inline-block;
	padding: 10px 20px;
	background-color: #1f2937;
	color: white;
	border-radius: 4px;
	cursor: pointer;
	transition: background-color 0.3s;
}

.hidden-file-input {
	display: none;
}

.profile-form div {
	display: flex;
	flex-direction: column;
}

.profile-form label {
	margin-bottom: 5px;
	font-weight: 500;
}

.profile-form input, .profile-form textarea {
	padding: 8px;
	border: 1px solid #ced4da;
	border-radius: 4px;
	font-size: 16px;
}

.profile-form input[readonly] {
	background-color: #e9ecef;
	cursor: not-allowed;
}

.btn-primary {
	padding: 10px 20px;
	background-color: #1f2937;
	color: #fff;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 16px;
	transition: background-color 0.3s;
	align-self: flex-start;
}

.btn-primary:hover {
	background-color: #0056b3;
}

/* Responsive Design */
@media ( max-width : 768px) {
	.dashboard-container {
		padding: 15px;
	}
	.profile-form {
		max-width: 100%;
	}
	.preview-image {
		width: 120px;
		height: 120px;
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
				method="post" enctype="multipart/form-data">
				<div class="pp">
					<img
						src="${pageContext.request.contextPath}/${user.profilePicture}"
						alt="Profile Picture" id="previewImage" class="preview-image">
				</div>

				<div class="change-pic-container">
					<label for="profilePicture" class="change-picture-btn">Change
						Profile Picture</label> <input type="file" id="profilePicture"
						name="profilePicture" class="hidden-file-input" accept="image/*">
				</div>
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
<script>
	document
			.getElementById('profilePicture')
			.addEventListener(
					'change',
					function(event) {
						const file = event.target.files[0];
						if (file) {
							const reader = new FileReader();
							reader.onload = function(e) {
								document.getElementById('previewImage').src = e.target.result;
							};
							reader.readAsDataURL(file);
						}
					});
</script>

</html>