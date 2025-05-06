<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
<style>
/* Loading animation styles */
.loader {
    display: none;
    border: 4px solid #f3f3f3;
    border-top: 4px solid #3498db;
    border-radius: 50%;
    width: 30px;
    height: 30px;
    animation: spin 1s linear infinite;
    margin: 10px auto;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

.register-button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}
</style>
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
                <c:if test="${not empty message}">
                    <p class="error-message">
                        <c:out value="${message}" />
                    </p>
                </c:if>
            </form>
        </div>
    </div>
    <div class="footer">@SoraNet 2025</div>

    <script>
        document.getElementById('registerForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const form = this;
            const button = document.getElementById('registerButton');
            const loader = document.getElementById('loader');
            const errorDisplay = document.getElementById('clientError');

            // Reset error message
            errorDisplay.style.display = 'none';
            errorDisplay.textContent = '';

            // Get form values
            const firstName = document.getElementById('firstName').value.trim();
            const lastName = document.getElementById('lastName').value.trim();
            const email = document.getElementById('email').value.trim();
            const phoneNumber = document.getElementById('phoneNumber').value.trim();
            const username = document.getElementById('username').value.trim();
            const address = document.getElementById('address').value.trim();
            const city = document.getElementById('city').value.trim();
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirm_password').value;
            const profilePicture = document.getElementById('profilePicture').files[0];

            // Validation functions
            function isNullOrEmpty(value) {
                return !value || value.length === 0;
            }

            function isAlphabetic(value) {
                return /^[A-Za-z]+$/.test(value);
            }

            function isValidEmail(value) {
                return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
            }

            function isValidPhoneNumber(value) {
                return /^98\d{8}$/.test(value);
            }

            function isAlphanumericStartingWithLetter(value) {
                return /^[a-zA-Z][a-zA-Z0-9]*$/.test(value);
            }

            function isValidPassword(value) {
                return /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/.test(value);
            }

            function doPasswordsMatch(password, confirmPassword) {
                return password === confirmPassword;
            }

            function isValidImageExtension(file) {
                if (!file) return true; // No file uploaded is valid
                const validExtensions = ['jpg', 'jpeg', 'png', 'gif'];
                const extension = file.name.split('.').pop().toLowerCase();
                return validExtensions.includes(extension);
            }

            // Perform validations
            let errorMessage = '';
            if (isNullOrEmpty(firstName)) {
                errorMessage = 'First name is required.';
            } else if (!isAlphabetic(firstName)) {
                errorMessage = 'First name must contain only letters.';
            } else if (isNullOrEmpty(lastName)) {
                errorMessage = 'Last name is required.';
            } else if (!isAlphabetic(lastName)) {
                errorMessage = 'Last name must contain only letters.';
            } else if (isNullOrEmpty(email)) {
                errorMessage = 'Email is required.';
            } else if (!isValidEmail(email)) {
                errorMessage = 'Invalid email format.';
            } else if (isNullOrEmpty(phoneNumber)) {
                errorMessage = 'Phone number is required.';
            } else if (!isValidPhoneNumber(phoneNumber)) {
                errorMessage = 'Phone number must be 10 digits starting with 98.';
            } else if (isNullOrEmpty(username)) {
                errorMessage = 'Username is required.';
            } else if (!isAlphanumericStartingWithLetter(username)) {
                errorMessage = 'Username must start with a letter and contain only letters and numbers.';
            } else if (isNullOrEmpty(address)) {
                errorMessage = 'Address is required.';
            } else if (isNullOrEmpty(city)) {
                errorMessage = 'City is required.';
            } else if (isNullOrEmpty(password)) {
                errorMessage = 'Password is required.';
            } else if (!isValidPassword(password)) {
                errorMessage = 'Password must be at least 8 characters, with 1 capital letter, 1 number, and 1 symbol.';
            } else if (!doPasswordsMatch(password, confirmPassword)) {
                errorMessage = 'Passwords do not match.';
            } else if (profilePicture && !isValidImageExtension(profilePicture)) {
                errorMessage = 'Profile picture must be a JPG, JPEG, PNG, or GIF file.';
            }

            if (errorMessage) {
                errorDisplay.textContent = errorMessage;
                errorDisplay.style.display = 'block';
                return;
            }

            // Show loading animation and submit form
            button.disabled = true;
            button.value = 'Registering...';
            loader.style.display = 'block';
            form.submit();
        });
    </script>
</body>
</html>