<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Plan Management</title>
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

.add-plan-button {
	margin-bottom: 1.5rem;
}

.add-plan-button button {
	padding: 0.75rem 1.5rem;
	border: none;
	border-radius: 4px;
	background-color: #1f2937;
	color: #fff;
	cursor: pointer;
	font-size: 1rem;
}

.add-plan-button button:hover {
	background-color: #374151;
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

.action-buttons button {
	padding: 0.5rem 1rem;
	border: none;
	border-radius: 4px;
	background-color: #1f2937;
	color: #fff;
	cursor: pointer;
	font-size: 0.9rem;
	margin-right: 0.5rem;
}

.action-buttons button:hover {
	background-color: #374151;
}

.modal {
	display: none;
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.5);
	justify-content: center;
	align-items: center;
	z-index: 1000;
}

.modal-content {
	background-color: #fff;
	padding: 1.5rem;
	border-radius: 8px;
	width: 90%;
	max-width: 600px;
}

.modal-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 1rem;
}

.modal-header h3 {
	margin: 0;
	color: #1f2937;
}

.modal-close {
	background: none;
	border: none;
	font-size: 1.2rem;
	cursor: pointer;
	color: #1f2937;
}

.modal-body label {
	display: block;
	margin-bottom: 0.5rem;
	font-weight: bold;
}

.modal-body input, .modal-body select, .modal-body textarea {
	width: 100%;
	padding: 0.75rem;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-bottom: 1rem;
}

.modal-body textarea {
	resize: vertical;
}

.modal-body input[type="checkbox"] {
	width: auto;
	margin-right: 0.5rem;
}

.modal-footer {
	display: flex;
	justify-content: flex-end;
	gap: 1rem;
}

.modal-footer button {
	padding: 0.75rem 1.5rem;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.modal-footer .btn-secondary {
	background-color: #6b7280;
	color: #fff;
}

.modal-footer .btn-primary {
	background-color: #1f2937;
	color: #fff;
}

.modal-footer .btn-secondary:hover {
	background-color: #4b5563;
}

.modal-footer .btn-primary:hover {
	background-color: #374151;
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
	.action-buttons button {
		padding: 0.4rem 0.8rem;
		font-size: 0.8rem;
	}
	.modal-content {
		width: 95%;
	}
}
</style>
</head>
<body>
	<div class="container-dash">
		<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
		<div class="dashboard-container">
			<h2>Plan Management</h2>

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

			<div class="add-plan-button">
				<button onclick="openAddModal()">Add New Plan</button>
			</div>

			<div class="search-bar">
				<input type="text" id="searchInput"
					placeholder="Search by plan name or speed..."
					oninput="filterPlans()">
			</div>

			<div class="table-container">
				<table class="table" id="plansTable">
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
							<th>Speed</th>
							<th>Price</th>
							<th>Duration</th>
							<th>Type</th>
							<th>Popular</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="plan" items="${plans}">
							<tr>
								<td>${plan.planId}</td>
								<td>${計劃.getPlanName()}</td>
								<td>${plan.speed}</td>
								<td>$${plan.price}</td>
								<td>${plan.planDuration}</td>
								<td>${plan.type}</td>
								<td>${plan.popular ? 'Yes' : 'No'}</td>
								<td class="action-buttons">
									<button
										onclick="openEditModal(${plan.planId}, '${plan.planName}', '${plan.speed}', ${plan.price}, '${plan.planDuration}', '${plan.planDescription}', '${plan.type}', '${plan.features}', ${plan.popular})">
										Edit</button>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<div class="modal" id="planModal">
				<div class="modal-content">
					<div class="modal-header">
						<h3 id="modalTitle">Add Plan</h3>
						<button class="modal-close" onclick="closePlanModal()">×</button>
					</div>
					<form id="planForm"
						action="${pageContext.request.contextPath}/admin/plans"
						method="post">
						<div class="modal-body">
							<input type="hidden" name="planId" id="modalPlanId">
							<div>
								<label for="modalPlanName">Plan Name</label> <input type="text"
									name="planName" id="modalPlanName" required>
							</div>
							<div>
								<label for="modalSpeed">Speed</label> <input type="text"
									name="speed" id="modalSpeed" required>
							</div>
							<div>
								<label for="modalPrice">Price</label> <input type="number"
									step="0.01" name="price" id="modalPrice" required>
							</div>
							<div>
								<label for="modalDuration">Duration</label> <input type="text"
									name="planDuration" id="modalDuration" required>
							</div>
							<div>
								<label for="modalDescription">Description</label>
								<textarea name="planDescription" id="modalDescription" rows="4"></textarea>
							</div>
							<div>
								<label for="modalType">Type</label> <select name="type"
									id="modalType" required>
									<option value="residential">Residential</option>
									<option value="business">Business</option>
								</select>
							</div>
							<div>
								<label for="modalFeatures">Features (comma-separated)</label>
								<textarea name="features" id="modalFeatures" rows="3"></textarea>
							</div>
							<div>
								<label> <input type="checkbox" name="popular"
									id="modalPopular" value="true"> Popular
								</label>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn-secondary"
								onclick="closePlanModal()">Close</button>
							<button type="submit" class="btn-primary">Save</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script>
        const plansTable = document.getElementById('plansTable');
        const searchInput = document.getElementById('searchInput');
        const planModal = document.getElementById('planModal');
        const planForm = document.getElementById('planForm');
        const modalTitle = document.getElementById('modalTitle');

        function filterPlans() {
            const query = searchInput.value.toLowerCase();
            const rows = plansTable.querySelectorAll('tbody tr');
            rows.forEach(row => {
                const name = row.cells[1].textContent.toLowerCase();
                const speed = row.cells[2].textContent.toLowerCase();
                row.style.display = name.includes(query) || speed.includes(query) ? '' : 'none';
            });
        }

        function openAddModal() {
            planForm.reset();
            modalTitle.textContent = 'Add Plan';
            document.getElementById('modalPlanId').value = '0';
            planModal.style.display = 'flex';
        }

        function openEditModal(planId, planName, speed, price, planDuration, planDescription, type, features, popular) {
            modalTitle.textContent = 'Edit Plan';
            document.getElementById('modalPlanId').value = planId;
            document.getElementById('modalPlanName').value = planName;
            document.getElementById('modalSpeed').value = speed;
            document.getElementById('modalPrice').value = price;
            document.getElementById('modalDuration').value = planDuration;
            document.getElementById('modalDescription').value = planDescription;
            document.getElementById('modalType').value = type;
            document.getElementById('modalFeatures').value = features.split(',').join(', ');
            document.getElementById('modalPopular').checked = popular;
            planModal.style.display = 'flex';
        }

        function closePlanModal() {
            planModal.style.display = 'none';
        }
    </script>
</body>
</html>