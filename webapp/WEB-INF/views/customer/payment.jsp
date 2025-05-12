<%@page import="com.soranet.model.PlanModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/payment.css">
</head>
<body>
	<%
	com.soranet.model.UserModel user = (com.soranet.model.UserModel) session.getAttribute("user");
	com.soranet.model.PlanModel selectedPlan = (com.soranet.model.PlanModel) request.getAttribute("selectedPlan");
	%>
	<%@ include file="/WEB-INF/views/components/header.jsp"%>
	<div class="container">
		<h1>Complete Your Payment</h1>

		<c:if test="${not empty selectedPlan}">
			<div class="payment-container">
				<div class="plan-summary">
					<h2>Order Summary</h2>
					<div class="plan-details">
						<h3>${selectedPlan.planName}</h3>
						<p>Speed: ${selectedPlan.speed}</p>
						<p>
							Price:
							<fmt:formatNumber value="${selectedPlan.price}" type="currency"
								currencySymbol="$" />
							/month
						</p>
					</div>
				</div>

				<form action="${pageContext.request.contextPath}/payment"
					method="post">
					<input type="hidden" name="planId" value="${selectedPlan.planId}">
					<input type="hidden" name="amount" value="${selectedPlan.price}">

					<div class="form-group">
						<label for="name">Full Name</label> <input
							value="${user.getFirstName()}" type="text" id="name" name="name"
							required>
					</div>

					<div class="form-group">
						<label for="email">Email</label> <input value="${user.getEmail()}"
							type="email" id="email" name="email" required>
					</div>

					<div class="form-group">
						<label for="address">Billing Address</label> <input
							value="${user.getAddress()}" type="text" id="address"
							name="address" required>
					</div>

					<div class="form-group">
						<label for="paymentMethod">Payment Method</label> <select
							id="paymentMethod" name="paymentMethod"
							onchange="togglePaymentFields()" required class="styled-select">
							<option value="credit" selected>Credit</option>
							<option value="esewa">eSewa</option>
							<option value="bank">Bank</option>
						</select>
					</div>

					<!-- Credit Card Fields -->
					<div id="creditFields">
						<div class="form-group">
							<label for="cardNumber">Card Number</label> <input type="text"
								id="cardNumber" name="cardNumber">
						</div>

						<div class="form-group">
							<label for="expiry">Expiration Date</label> <input type="month"
								id="expiry" name="expiry">
						</div>

						<div class="form-group">
							<label for="cvv">CVV</label> <input type="text" id="cvv"
								name="cvv" pattern="[0-9]{3,4}">
						</div>
					</div>

					<!-- eSewa Fields -->
					<div id="esewaFields" style="display: none;">
						<div class="form-group">
							<label for="esewaId">eSewa ID</label> <input type="text"
								id="esewaId" name="esewaId">
						</div>

						<div class="form-group">
							<label for="esewaPassword">eSewa Password</label> <input
								type="password" id="esewaPassword" name="esewaPassword">
						</div>
					</div>

					<!-- Bank Fields -->
					<div id="bankFields" style="display: none;">
						<div class="form-group">
							<label for="bankId">Bank ID</label> <input type="text"
								id="bankId" name="bankId">
						</div>

						<div class="form-group">
							<label for="bankPassword">Bank Password</label> <input
								type="password" id="bankPassword" name="bankPassword">
						</div>
					</div>

					<button type="submit" class="btn btn-primary">Complete
						Payment</button>
					<a href="${pageContext.request.contextPath}/plans" class="btn">Cancel</a>
				</form>

			</div>
		</c:if>

		<c:if test="${empty selectedPlan}">
			<div class="error">
				<h2>No plan selected</h2>
				<p>Please select a plan first</p>
				<a href="${pageContext.request.contextPath}/plans"
					class="btn btn-primary"> View Plans </a>
			</div>
		</c:if>
	</div>
	<%@ include file="/WEB-INF/views/components/footer.jsp"%>
	<script>
	function togglePaymentFields() {
	    const method = document.getElementById("paymentMethod").value;

	    document.getElementById("creditFields").style.display = (method === "credit") ? "block" : "none";
	    document.getElementById("esewaFields").style.display = (method === "esewa") ? "block" : "none";
	    document.getElementById("bankFields").style.display = (method === "bank") ? "block" : "none";
	}

	// Initial call to handle pre-selected default
	window.onload = togglePaymentFields;
	</script>

</body>
</html>