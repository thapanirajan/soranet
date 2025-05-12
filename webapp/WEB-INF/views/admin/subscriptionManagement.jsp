<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Subscription Management</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/subscriptionManagement.css">
<style>
.action-bar button {
	padding: 0.75rem 1.5rem;
	background-color: #1f2937;
	color: #fff;
	margin-bottom: 1rem;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1rem;
	transition: background-color 0.3s ease;
}

.action-bar button:hover {
	background-color: #374151;
}


.action-bar button:hover {
	background-color: #374151;
}
</style>
</head>
<body>
	<div class="container-dash">
		<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
		<div class="dashboard-container">
			<h2>Subscription Management</h2>

			<c:if test="${not empty successMessage}">
				<div class="alert alert-success">
					<c:out value="${successMessage}" />
					<button class="alert-close"
						onclick="this.parentElement.style.display='none'"
						aria-label="Close alert">×</button>
				</div>
			</c:if>
			<c:if test="${not empty errorMessage}">
				<div class="alert alert-error">
					<c:out value="${errorMessage}" />
					<button class="alert-close"
						onclick="this.parentElement.style.display='none'"
						aria-label="Close alert">×</button>
				</div>
			</c:if>

			<div class="action-bar">
				<button onclick="openCreateModal()"
					aria-label="Add new subscription">Add New Subscription</button>
				<div class="search-bar">
					<input type="text" id="searchInput" name="searchInput"
						placeholder="Search by user ID or plan ID..."
						oninput="filterSubscriptions()">
				</div>
			</div>

			<div class="table-container">
				<table class="table" id="subscriptionsTable"
					aria-label="Subscription Management Table">
					<thead>
						<tr>
							<th>ID</th>
							<th>User ID</th>
							<th>Plan ID</th>
							<th>Start Date</th>
							<th>End Date</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="subscription" items="${subscriptions}">
							<tr>
								<td><c:out value="${subscription.subscriptionId}" /></td>
								<td><c:out value="${subscription.userId}" /></td>
								<td><c:out value="${subscription.planId}" /></td>
								<td><c:out
										value="${subscription.startDate != null ? subscription.startDate : 'N/A'}" /></td>
								<td><c:out
										value="${subscription.endDate != null ? subscription.endDate : 'N/A'}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<!-- Create Subscription Modal -->
			<div class="modal" id="createSubscriptionModal">
				<div class="modal-content">
					<div class="modal-header">
						<h3>Create Subscription</h3>
						<button class="modal-close" onclick="closeCreateModal()"
							aria-label="Close modal">×</button>
					</div>
					<form id="createSubscriptionForm"
						action="${pageContext.request.contextPath}/admin/subscriptions"
						method="post">
						<div class="modal-body">
							<div>
								<label for="createUserId">User ID</label> <input type="number"
									name="userId" id="createUserId" required>
							</div>
							<div>
								<label for="createPlanId">Plan ID</label> <input type="number"
									name="planId" id="createPlanId" required>
							</div>
							<div>
								<label for="createStartDate">Start Date</label> <input
									type="date" name="startDate" id="createStartDate" required>
							</div>
							<div>
								<label for="createEndDate">End Date</label> <input type="date"
									name="endDate" id="createEndDate" required>
							</div>
							<div>
								<label for="createPaymentMethod">Payment Method</label> <select
									name="paymentMethod" id="createPaymentMethod" required>
									<option value="credit">Credit Card</option>
									<option value="paypal">Esewa</option>
									<option value="bank">Bank</option>
								</select>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn-secondary"
								onclick="closeCreateModal()">Close</button>
							<button type="submit" class="btn-primary">Save</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script>
        const subscriptionsTable = document.getElementById('subscriptionsTable');
        const searchInput = document.getElementById('searchInput');
        const createSubscriptionModal = document.getElementById('createSubscriptionModal');

        function filterSubscriptions() {
            const query = searchInput.value.toLowerCase();
            const rows = subscriptionsTable.querySelectorAll('tbody tr');
            rows.forEach(row => {
                const userId = row.cells[1].textContent.toLowerCase();
                const planId = row.cells[2].textContent.toLowerCase();
                row.style.display = userId.includes(query) || planId.includes(query) ? '' : 'none';
            });
        }

        function openCreateModal() {
            document.getElementById('createSubscriptionForm').reset();
            createSubscriptionModal.style.display = 'flex';
        }

        function closeCreateModal() {
            createSubscriptionModal.style.display = 'none';
        }
    </script>
</body>
</html>