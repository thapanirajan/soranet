
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Admin Dashboard</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/dashboard.css">
</head>
<body>
	<div class="dash-container">
		<div class="dashboard-head">
			<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
		</div>
		<div class="dashboard-container">
			<h2>Dashboard</h2>
			<div class="dashboard-cards">
				<c:if test="${not empty dashboardData}">
					<div class="card">
						<span>👤</span>
						<div>
							<h5>Total Users</h5>
							<h6>
								<c:out value="${dashboardData.totalUsers}" />
							</h6>
						</div>
					</div>
					<div class="card">
						<span>📋</span>
						<div>
							<h5>Total Subscriptions</h5>
							<h6>
								<c:out value="${dashboardData.totalSubscriptions}" />
							</h6>
						</div>
					</div>
					<div class="card">
						<span>💰</span>
						<div>
							<h5>Total Revenue</h5>
							<h6>
								<c:out value="${dashboardData.totalRevenue}" />
							</h6>
						</div>
					</div>
					<div class="card">
						<span>📊</span>
						<div>
							<h5>Total Plans</h5>
							<h6>
								<c:out value="${dashboardData.totalPlans}" />
							</h6>
						</div>
					</div>
				</c:if>
			</div>

			<h3>Subscription Breakdown by Plan Type</h3>
			<c:choose>
				<c:when test="${empty dashboardData.subscriptionsByPlanType}">
					<p>No subscription data available.</p>
				</c:when>
				<c:otherwise>
					<table aria-label="Subscription Breakdown Table">
						<thead>
							<tr>
								<th>Plan Type</th>
								<th>Subscription Count</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="entry"
								items="${dashboardData.subscriptionsByPlanType}">
								<tr>
									<td><c:out value="${entry.type}" /></td>
									<td><c:out value="${entry.subscription_count}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>

			<h3>Recent Users</h3>
			<c:choose>
				<c:when test="${empty dashboardData.recentUsers}">
					<p>No recent users found.</p>
				</c:when>
				<c:otherwise>
					<table id="userTable" aria-label="Recent Users Table">
						<thead>
							<tr>
								<th>User ID</th>
								<th>Username</th>
								<th>Email</th>
								<th>Joined At</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="user" items="${dashboardData.recentUsers}">
								<tr>
									<td><c:out value="${user.userId}" /></td>
									<td><c:out value="${user.username}" /></td>
									<td><c:out value="${user.email}" /></td>
									<td><c:out value="${user.createdAt}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>

			<h3>Recent Subscriptions</h3>
			<c:choose>
				<c:when test="${empty dashboardData.recentSubscriptions}">
					<p>No recent subscriptions found.</p>
				</c:when>
				<c:otherwise>
					<table id="subscriptionTable"
						aria-label="Recent Subscriptions Table">
						<thead>
							<tr>
								<th>Subscription ID</th>
								<th>Username</th>
								<th>Plan</th>
								<th>Start</th>
								<th>End</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="sub" items="${dashboardData.recentSubscriptions}">
								<tr>
									<td><c:out value="${sub.subscriptionId}" /></td>
									<td><c:out value="${sub.username}" /></td>
									<td><c:out value="${sub.planName}" /></td>
									<td><c:out value="${sub.startDate}" /></td>
									<td><c:out value="${sub.endDate}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>

			<h3>Recent Payments</h3>
		
			<c:choose>
				<c:when test="${empty dashboardData.recentPayments}">
					<p>No recent payments found.</p>
				</c:when>
				<c:otherwise>
					<table id="paymentTable" aria-label="Recent Payments Table">
						<thead>
							<tr>
								<th>Payment ID</th>
								<th>Username</th>
								<th>Amount</th>
								<th>Date</th>
								<th>Method</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="pay" items="${dashboardData.recentPayments}">
								<tr>
									<td><c:out value="${pay.paymentId}" /></td>
									<td><c:out value="${pay.username}" /></td>
									<td><c:out value="${pay.amount}" /></td>
									<td><c:out value="${pay.paymentDate}" /></td>
									<td><c:out value="${pay.paymentMethod}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>

