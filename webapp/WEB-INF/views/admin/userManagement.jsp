<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - User Management</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: Arial, sans-serif;
	background-color: #f8f9fa;
	display: flex;
}

div {
	border: 1px solid black;
}

.container-dash {
	display: flex;
	max-height: 100dvh;
	overflow: auto;
}

.dashboard-container {
	flex-grow: 1;
	padding: 2rem;
	max-height: 100dvh;
	overflow: auto;
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

.action-buttons button {
	padding: 0.5rem 1rem;
	border: none;
	border-radius: 4px;
	background-color: #1f2937;
	color: #fff;
	cursor: pointer;
	font-size: 0.9rem;
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
	max-width: 500px;
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

.modal-body input, .modal-body select {
	width: 100%;
	padding: 0.75rem;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-bottom: 1rem;
}

.modal-body input[readonly] {
	background-color: #e5e7eb;
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
	<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
	<div class="dashboard-container">
		<h2>User Management</h2>

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
				placeholder="Search by name or email..." oninput="filterUsers()">
		</div>

		<div class="table-container">
			<table class="table" id="usersTable">
				<thead>
					<tr>
						<th>ID</th>
						<th>Name</th>
						<th>Email</th>
						<th>Role</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items="${users}">
						<tr>
							<td>${user.userId}</td>
							<td>${user.firstName}${user.lastName}</td>
							<td>${user.email}</td>
							<td>${user.role}</td>
							<td class="action-buttons">
								<button
									onclick="openEditModal(${user.userId}, '${user.firstName} ${user.lastName}', '${user.role}')">
									Edit Role</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="modal" id="editRoleModal">
			<div class="modal-content">
				<div class="modal-header">
					<h3>Edit User Role</h3>
					<button class="modal-close" onclick="closeEditModal()">×</button>
				</div>
				<form id="editRoleForm"
					action="${pageContext.request.contextPath}/admin/users"
					method="post">
					<div class="modal-body">
						<input type="hidden" name="userId" id="modalUserId">
						<div>
							<label for="modalUserName">User Name</label> <input type="text"
								id="modalUserName" readonly>
						</div>
						<div>
							<label for="modalUserRole">Role</label> <select name="role"
								id="modalUserRole" required>
								<option value="customer">Customer</option>
								<option value="admin">Admin</option>
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn-secondary"
							onclick="closeEditModal()">Close</button>
						<button type="submit" class="btn-primary">Save Changes</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script>
        const usersTable = document.getElementById('usersTable');
        const searchInput = document.getElementById('searchInput');
        const editModal = document.getElementById('editRoleModal');

        function filterUsers() {
            const query = searchInput.value.toLowerCase();
            const rows = usersTable.querySelectorAll('tbody tr');
            rows.forEach(row => {
                const name = row.cells[1].textContent.toLowerCase();
                const email = row.cells[2].textContent.toLowerCase();
                row.style.display = name.includes(query) || email.includes(query) ? '' : 'none';
            });
        }

        function openEditModal(userId, userName, userRole) {
            document.getElementById('modalUserId').value = userId;
            document.getElementById('modalUserName').value = userName;
            document.getElementById('modalUserRole').value = userRole.toLowerCase();
            editModal.style.display = 'flex';
        }

        function closeEditModal() {
            editModal.style.display = 'none';
        }
    </script>
</body>
</html>