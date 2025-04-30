<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Profile - SoraNet</title>
<script src="https://cdn.tailwindcss.com"></script>

</head>
<body>
	<%@ include file="/WEB-INF/views/components/header.jsp"%>
<div class="container mx-auto px-4 py-10 ">
    <div class="profile-form-box max-w-2xl mx-auto bg-white p-8 rounded-lg shadow-lg">
        <h1 class="profile-title text-3xl font-bold text-gray-800 mb-8 text-center">Edit Profile</h1>
        <form action="${pageContext.request.contextPath}/profile" method="POST" enctype="multipart/form-data">
            <!-- Profile Picture -->
            <div class="profile-picture-section flex flex-col items-center mb-8">
                <div class="profile-picture w-32 h-32 rounded-full overflow-hidden border-4 border-gray-200 mb-4 flex items-center justify-center bg-gray-100" id="profilePicturePreview">
                    <c:choose>
                        <c:when test="${not empty user.profilePicture}">
                            <img src="/resources/uploads/${user.profilePicture}" alt="Profile Picture" id="previewImage" class="w-full h-full object-cover">
                        </c:when>
                        <c:otherwise>
                            <span class="text-gray-500 text-sm">No Image</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div>
                    <label for="profilePicture" class="change-picture cursor-pointer text-indigo-600 hover:text-indigo-800 font-medium">Change Picture</label>
                    <input type="file" id="profilePicture" name="profilePicture" accept="image/*" class="hidden">
                </div>
            </div>

            <!-- First Name -->
            <div class="form-group mb-6">
                <label for="firstName" class="form-label block text-sm font-medium text-gray-700 mb-2">First Name</label>
                <input type="text" id="firstName" name="firstName" class="form-input w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500" value="${user.firstName}" required>
            </div>

            <!-- Last Name -->
            <div class="form-group mb-6">
                <label for="lastName" class="form-label block text-sm font-medium text-gray-700 mb-2">Last Name</label>
                <input type="text" id="lastName" name="lastName" class="form-input w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500" value="${user.lastName}" required>
            </div>

            <!-- Username -->
            <div class="form-group mb-6">
                <label for="username" class="form-label block text-sm font-medium text-gray-700 mb-2">Username</label>
                <input type="text" id="username" name="username" class="form-input w-full px-4 py-2 border border-gray-300 rounded-md bg-gray-100 cursor-not-allowed" value="${user.username}" disabled>
            </div>

            <!-- Email -->
            <div class="form-group mb-6">
                <label for="email" class="form-label block text-sm font-medium text-gray-700 mb-2">Email</label>
                <input type="email" id="email" name="email" class="form-input w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500" value="${user.email}" required>
            </div>

            <!-- Phone Number -->
            <div class="form-group mb-6">
                <label for="phoneNumber" class="form-label block text-sm font-medium text-gray-700 mb-2">Phone Number</label>
                <input type="text" id="phoneNumber" name="phoneNumber" class="form-input w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500" value="${user.phoneNumber}" required>
            </div>

            <!-- Address -->
            <div class="form-group mb-6">
                <label for="address" class="form-label block text-sm font-medium text-gray-700 mb-2">Address</label>
                <input type="text" id="address" name="address" class="form-input w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500" value="${user.address}">
            </div>

            <!-- City -->
            <div class="form-group mb-8">
                <label for="city" class="form-label block text-sm font-medium text-gray-700 mb-2">City</label>
                <input type="text" id="city" name="city" class="form-input w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500" value="${user.city}">
            </div>

            <!-- Submit Button -->
            <button type="submit" class="submit-button w-full bg-indigo-600 text-white py-3 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition duration-200">Save</button>

            <!-- Error Message -->
            <c:if test="${not empty message}">
                <p class="error-message text-red-500 text-center mt-4">
                    <c:out value="${message}" />
                </p>
            </c:if>
        </form>
    </div>
</div>

<script>
    // Handle profile picture preview
    document.getElementById('profilePicture').addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const previewDiv = document.getElementById('profilePicturePreview');
                previewDiv.innerHTML = '';
                const img = document.createElement('img');
                img.src = e.target.result;
                img.id = 'previewImage';
                img.alt = 'Profile Picture Preview';
                img.className = 'w-full h-full object-cover';
                previewDiv.appendChild(img);
            };
            reader.readAsDataURL(file);
        }
    });
</script>
</body>
</html>