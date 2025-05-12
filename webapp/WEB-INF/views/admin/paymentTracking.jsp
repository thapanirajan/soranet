<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Payment Tracking</title>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/paymentTracking.css">
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
					placeholder="Search by subscription ID "
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
            const query = searchInput.value.toLowerCase().trim();
            const rows = paymentsTable.querySelectorAll('tbody tr');
            rows.forEach(row => {
                const paymentId = row.cells[0].textContent.toLowerCase();
                row.style.display = paymentId.includes(query) ? '' : 'none';
            });
        }
    </script>
    
</body>
</html>