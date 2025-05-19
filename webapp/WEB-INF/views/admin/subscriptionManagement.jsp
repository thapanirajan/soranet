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

.container-dashboard {
	display: flex;
	min-height: 100vh;
	gap: 1rem;
}
.action-bar button {
	padding: 0.75rem 1.5rem;
	background-color: #1f2937;
	color: #fff;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1rem;
	transition: background-color 0.3s ease;
}

.action-bar button:hover {
	background-color: #374151;
}

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

.admin-nav{
	position: fixed;
	left: 0;
	left: 0;
}

.dashboard-container {
	flex: 1;
	padding: 2rem;
	margin-left: 17rem;
	overflow-y: auto;
	z-index: 1000;
}
</style>
</head>
<body>
	<div class="container-dashboard">
		<div class="admin-nav">
			<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
		</div>
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
					aria-label="Add new subscription" style="margin-bottom:1rem;">Add New Subscription</button>
				<div class="search-bar">
					<form
						action="${pageContext.request.contextPath}/admin/subscriptions"
						method="get">
						<input type="text" name="searchQuery"
							placeholder="Search by user ID or plan ID"
							value="${param.searchQuery}">
						<button type="submit">Search</button>
						<c:if test="${not empty param.searchQuery}">
							<a href="${pageContext.request.contextPath}/admin/subscriptions">Clear</a>
						</c:if>
					</form>
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
		const createSubscriptionModal = document
				.getElementById('createSubscriptionModal');

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