<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Profile - SoraNet</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body>
	<%@ include file="/WEB-INF/views/components/header.jsp"%>
	<div class="container">
		<div class="profile-form-box">
			<h1 class="profile-title">Edit Profile</h1>
			<form action="${pageContext.request.contextPath}/user/profile"
				method="POST" enctype="multipart/form-data">
				<!-- Profile Picture -->
				<div class="profile-picture-section">
					<div class="profile-picture" id="profilePicturePreview">
						<c:choose>
							<c:when test="${not empty user.profilePicture}">
								<!-- Confirm actual rendered path -->
								<img
									src="${pageContext.request.contextPath}/${user.profilePicture}"
									alt="Profile Picture" id="previewImage" class="preview-image">
							</c:when>
							<c:otherwise>
								<span class="no-image-text">No Image</span>
							</c:otherwise>
						</c:choose>
					</div>
					<div>
						<label for="profilePicture" class="change-picture">Change
							Picture</label> <input type="file" id="profilePicture"
							name="profilePicture" class="hidden">
					</div>
				</div>

				<!-- First Name -->
				<div class="form-group">
					<label for="firstName" class="form-label">First Name</label> <input
						type="text" id="firstName" name="firstName" class="form-input"
						value="${user.firstName}" required>
				</div>

				<!-- Last Name -->
				<div class="form-group">
					<label for="lastName" class="form-label">Last Name</label> <input
						type="text" id="lastName" name="lastName" class="form-input"
						value="${user.lastName}" required>
				</div>

				<!-- Username -->
				<div class="form-group">
					<label for="username" class="form-label">Username</label> <input
						type="text" id="username" name="username"
						class="form-input disabled-input" value="${user.username}"
						disabled>
				</div>

				<!-- Email -->
				<div class="form-group">
					<label for="email" class="form-label">Email</label> <input
						type="email" id="email" name="email" class="form-input"
						value="${user.email}" required>
				</div>

				<!-- Phone Number -->
				<div class="form-group">
					<label for="phoneNumber" class="form-label">Phone Number</label> <input
						type="text" id="phoneNumber" name="phoneNumber" class="form-input"
						value="${user.phoneNumber}" required>
				</div>

				<!-- Address -->
				<div class="form-group">
					<label for="address" class="form-label">Address</label> <input
						type="text" id="address" name="address" class="form-input"
						value="${user.address}">
				</div>

				<!-- City -->
				<div class="form-group">
					<label for="city" class="form-label">City</label> <input
						type="text" id="city" name="city" class="form-input"
						value="${user.city}">
				</div>

				<!-- Submit Button -->
				<button type="submit" class="submit-button">Save</button>

				<!-- Error Message -->
				<c:if test="${not empty message}">
					<p class="error-message">
						<c:out value="${message}" />
					</p>
				</c:if>
			</form>
		</div>
	</div>
	<script>
		console
				.log("${pageContext.request.contextPath}/${user.profilePicture}");
		// Handle profile picture preview
		document.getElementById('profilePicture').addEventListener(
				'change',
				function(event) {
					const file = event.target.files[0];
					if (file) {
						const reader = new FileReader();
						reader.onload = function(e) {
							const previewDiv = document
									.getElementById('profilePicturePreview');
							previewDiv.innerHTML = '';
							const img = document.createElement('img');
							img.src = e.target.result;
							img.id = 'previewImage';
							img.alt = 'Profile Picture Preview';
							img.className = 'preview-image';
							previewDiv.appendChild(img);
						};
						reader.readAsDataURL(file);
					}
				});
	</script>
</body>
</html>