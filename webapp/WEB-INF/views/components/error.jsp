<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Error</title>
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
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
}

h2 {
	margin-bottom: 1.5rem;
	color: #1f2937;
	text-align: center;
}

.error-box {
	background-color: #fff;
	padding: 2rem;
	border-radius: 8px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
	max-width: 600px;
	width: 100%;
	text-align: center;
}

.error-message {
	color: #e74c3c;
	margin-bottom: 1.5rem;
	font-size: 1.1rem;
}

.back-button {
	padding: 0.75rem 1.5rem;
	border: none;
	border-radius: 4px;
	background-color: #1f2937;
	color: #fff;
	cursor: pointer;
	font-size: 1rem;
	text-decoration: none;
}

.back-button:hover {
	background-color: #374151;
}

@media ( max-width : 768px) {
	.dashboard-container {
		padding: 1rem;
	}
	.error-box {
		padding: 1rem;
	}
	.error-message {
		font-size: 1rem;
	}
	.back-button {
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
			<h2>Error</h2>
			<div class="error-box">
				<div class="error-message">
					<c:out value="${errorMessage}"
						default="An unexpected error occurred. Please try again later." />
				</div>
				<a href="${pageContext.request.contextPath}/admin/dashboard"
					class="back-button">Back to Dashboard</a>
			</div>
		</div>
	</div>
</body>
</html>