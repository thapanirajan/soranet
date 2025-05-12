
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - Plan Management</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/planManagement.css">
	<style>
	.dashboard-container {
	flex-grow: 1;
	padding: 2rem;
	margin-left: 40px;
	margin-top: 80px; 
	overflow-y: auto;
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

			<div class="add-plan-button">
				<button onclick="openAddModal()" aria-label="Add new plan">Add
					New Plan</button>
			</div>

			<div class="search-bar">
				<label for="searchInput">Search by plan name or speed:</label> <input
					type="text" id="searchInput"
					placeholder="Search by plan name or speed...">
			</div>

			<div class="table-container">
				<table class="table" id="plansTable"
					aria-label="Plan Management Table">
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
								<td><c:out value="${plan.planId}" /></td>
								<td><c:out value="${plan.planName}" /></td>
								<td><c:out value="${plan.speed}" /></td>
								<td>$<c:out value="${plan.price}" /></td>
								<td><c:out value="${plan.planDuration}" /></td>
								<td><c:out value="${plan.type}" /></td>
								<td><c:out value="${plan.popular ? 'Yes' : 'No'}" /></td>
								<td class="action-buttons">
									<button
										onclick="openEditModal(<c:out value='${plan.planId}' />, '<c:out value="${plan.planName}" />', '<c:out value="${plan.speed}" />', <c:out value='${plan.price}' />, '<c:out value="${plan.planDuration}" />', '<c:out value="${plan.planDescription}" />', '<c:out value="${plan.type}" />', '<c:out value="${plan.features}" />', <c:out value='${plan.popular}' />)"
										aria-label="Edit plan <c:out value='${plan.planName}' />">Edit</button>
									<form action="${pageContext.request.contextPath}/admin/plans"
										method="post" style="display: inline;">
										<input type="hidden" name="_method" value="DELETE"> <input
											type="hidden" name="planId" value="${plan.planId}">

										<button type="submit" class="delete-button"
											onclick="return confirm('Are you sure you want to delete plan ${plan.planName}?')"
											aria-label="Delete plan ${plan.planName}">Delete</button>
									</form>
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
						<button class="modal-close" onclick="closePlanModal()"
							aria-label="Close modal">×</button>
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
        const planModal = document.getElementById('planModal');
        const planForm = document.getElementById('planForm');
        const modalTitle = document.getElementById('modalTitle');

        // Add event listener for search input
        document.getElementById('searchInput').addEventListener('input', function(event) {
            filterPlans(event.target.value);
        });

        function filterPlans(query) {
            console.log(`Filtering plans with query: ${query}`);
            const plansTable = document.getElementById('plansTable');
            if (!plansTable) {
                console.error('Plans table not found');
                return;
            }
            const rows = plansTable.querySelectorAll('tbody tr');
            query = query.toLowerCase().trim();
            rows.forEach(row => {
                const planId = row.cells[0].textContent.toLowerCase();
                const planName = row.cells[1].textContent.toLowerCase();
                row.style.display = planId.includes(query) || planName.includes(query) ? '' : 'none';
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
            document.getElementById('modalDescription').value = planDescription || '';
            document.getElementById('modalType').value = type;
            document.getElementById('modalFeatures').value = features ? features.split(',').join(', ') : '';
            document.getElementById('modalPopular').checked = popular === 'true';
            planModal.style.display = 'flex';
        }

        function closePlanModal() {
            planModal.style.display = 'none';
        }
    </script>
</body>
</html>

