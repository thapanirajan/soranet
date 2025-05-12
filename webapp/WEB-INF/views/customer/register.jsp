<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
    <div class="header">
        <a class="logo" href="${pageContext.request.contextPath}/ ">SoraNet</a>
        <div class="signup-link">
            Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a>
        </div>
    </div>
    <div class="container">
        <div class="register-box">
            <h1 class="register-title">Register</h1>
            <form action="${pageContext.request.contextPath}/register" method="POST" class="register-form" id="registerForm" enctype="multipart/form-data">
                <div class="input-group">
                    <input type="text" name="firstName" id="firstName" placeholder="First Name" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="text" name="lastName" id="lastName" placeholder="Last Name" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="email" name="email" id="email" placeholder="Email" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="tel" name="phoneNumber" id="phoneNumber" placeholder="Phone Number" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="text" name="username" id="username" placeholder="Username" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="text" name="address" id="address" placeholder="Address" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="text" name="city" id="city" placeholder="City" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="password" name="password" id="password" placeholder="Password" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="password" name="confirm_password" id="confirm_password" placeholder="Confirm Password" class="input-field" required>
                    <div class="underline"></div>
                </div>
                <div class="input-group">
                    <input type="file" accept="image/*" name="profilePicture" id="profilePicture" class="input-field">
                    <div class="underline"></div>
                </div>
                <div class="button-group">
                    <input type="submit" value="Register" class="register-button" id="registerButton">
                    <div class="loader" id="loader"></div>
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
</body>
</html>