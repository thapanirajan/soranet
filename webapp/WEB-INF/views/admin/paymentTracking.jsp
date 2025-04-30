<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Payment Tracking</title>
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

.search-bar {
	margin-bottom: 1.5rem;
	max-width: 400px;
}

.search-bar input {
	width: 100%;
	padding: 0.75rem;
	border: 1px solid #ccc;
	border-radius: 4px;
	font-size: 1rem;
}

.table-container {
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
	overflow-x: auto;
}

.table {
	width: 100%;
	border-collapse: collapse;
}

.table th, .table td {
	padding: 1rem;
	text-align: left;
	border-bottom: 1px solid #e5e7eb;
}

.table th {
	background-color: #1f2937;
	color: #fff;
}

.table tr:hover {
	background-color: #f3f4f6;
}

@media ( max-width : 768px) {
	.dashboard-container {
		padding: 1rem;
	}
	.search-bar {
		max-width: 100%;
	}
	.table th, .table td {
		padding: 0.5rem;
		font-size: 0.9rem;
	}
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
				<input type="text" id="searchInput"
					placeholder="Search by subscription ID or amount..."
					oninput="filterPayments()">
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

	<script>
        const paymentsTable = document.getElementById('paymentsTable');
        const searchInput = document.getElementById('searchInput');

        function filterPayments() {
            const query = searchInput.value.toLowerCase();
            const rows = paymentsTable.querySelectorAll('tbody tr');
            rows.forEach(row => {
                const subscriptionId = row.cells[1].textContent.toLowerCase();
                const amount = row.cells[2].textContent.toLowerCase();
                row.style.display = subscriptionId.includes(query) || amount.includes(query) ? '' : 'none';
            });
        }
    </script>
</body>
</html>