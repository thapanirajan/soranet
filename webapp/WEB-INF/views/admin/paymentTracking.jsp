<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Payment Tracking</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/paymentTracking.css">
<style>
.search-bar form {
	display: flex;
	align-items: center;
	gap: 10px;
}

.search-bar button[type="submit"] {
	background-color: #007bff;
	color: white;
	border: none;
	padding: 8px 16px;
	border-radius: 4px;
	cursor: pointer;
	font-size: 14px;
	transition: background-color 0.3s ease;
}

.search-bar button[type="submit"]:hover {
	background-color: #0056b3;
}

.search-bar a {
	color: #dc3545;
	text-decoration: none;
	font-size: 14px;
	padding: 8px 12px;
	border: 1px solid #dc3545;
	border-radius: 4px;
	transition: background-color 0.3s ease, color 0.3s ease;
}

.search-bar a:hover {
	background-color: #dc3545;
	color: white;
}
</style>
</head>
<body>
	<div class="container-dash">
		<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
		<div class="dashboard-container">
			<h2>Payment Tracking</h2>

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

			<div class="search-bar">
				<form action="${pageContext.request.contextPath}/admin/payments"
					method="get">
					<input type="text" name="subscriptionId"
						placeholder="Search by subscription ID"
						value="${param.subscriptionId}">
					<button type="submit">Search</button>
					<c:if test="${not empty param.subscriptionId}">
						<a href="${pageContext.request.contextPath}/admin/payments">Clear</a>
					</c:if>
				</form>
			</div>

			<div class="table-container">
				<table class="table" id="paymentsTable">
					<thead>
						<tr>
							<th>ID</th>
							<th>Subscription ID</th>
							<th>Amount</th>
							<th>Payment Date</th>
							<th>Payment Method</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="payment" items="${payments}">
							<tr>
								<td>${payment.paymentId}</td>
								<td>${payment.subscriptionId}</td>
								<td>$${payment.amount}</td>
								<td>${payment.paymentDate}</td>
								<td>${payment.paymentMethod}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>