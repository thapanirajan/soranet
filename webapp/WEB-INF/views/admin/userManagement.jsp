
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>SoraNet - User Management</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/userManagement.css">
</head>
<body>
	<div class="container-dash">
		<%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
		<div class="dashboard-container">
			<h2>User Management</h2>

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

			<div class="add-user-button">
				<button onclick="openAddModal()" aria-label="Add new user">Add
					New User</button>
			</div>

			<div class="search-bar">
				<label for="searchInput">Search by name or email:</label> <input
					type="text" id="searchInput"
					placeholder="Search by name or email...">
			</div>

			<div class="table-container">
				<c:choose>
					<c:when test="${empty users}">
						<p>No users found.</p>
					</c:when>
					<c:otherwise>
						<table class="table" id="usersTable"
							aria-label="User Management Table">
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
										<td><c:out value="${user.userId}" /></td>
										<td><c:out value="${user.firstName} ${user.lastName}" /></td>
										<td><c:out value="${user.email}" /></td>
										<td><c:out value="${user.role}" /></td>
										<td class="action-buttons">
											<button
												onclick="openEditModal(<c:out value='${user.userId}' />, '<c:out value="${user.firstName} ${user.lastName}" />', '<c:out value="${user.role}" />')"
												aria-label="Edit role for <c:out value='${user.firstName} ${user.lastName}' />">Edit
												Role</button>
											<form action="${pageContext.request.contextPath}/admin/users"
												method="post" style="display: inline;">
												<input type="hidden" name="_method" value="DELETE">
												<input type="hidden" name="userId" value="${user.userId}">
												<button type="submit" class="delete-button"
													onclick="return confirm('Are you sure you want to delete user <c:out value="${user.firstName} ${user.lastName}" />?')"
													aria-label="Delete user <c:out value='${user.firstName} ${user.lastName}' />">Delete</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>

			<!-- Edit Role Modal -->
			<div class="modal" id="editRoleModal">
				<div class="modal-content">
					<div class="modal-header">
						<h3>Edit User Role</h3>
						<button class="modal-close" onclick="closeEditModal()"
							aria-label="Close edit modal">×</button>
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

			<!-- Add User Modal -->
			<div class="modal" id="addUserModal">
				<div class="modal-content">
					<div class="modal-header">
						<h3>Add New User</h3>
						<button class="modal-close" onclick="closeAddModal()"
							aria-label="Close add user modal">×</button>
					</div>
					<form id="addUserForm"
						action="${pageContext.request.contextPath}/admin/users"
						method="post">
						<div class="modal-body">
							<input type="hidden" name="userId" value="0">
							<div>
								<label for="addUsername">Username</label> <input type="text"
									name="username" id="addUsername" required>
							</div>
							<div>
								<label for="addPassword">Password</label> <input type="password"
									name="password" id="addPassword" required>
							</div>
							<div>
								<label for="addFirstName">First Name</label> <input type="text"
									name="firstName" id="addFirstName" required>
							</div>
							<div>
								<label for="addLastName">Last Name</label> <input type="text"
									name="lastName" id="addLastName" required>
							</div>
							<div>
								<label for="addEmail">Email</label> <input type="email"
									name="email" id="addEmail" required>
							</div>
							<div>
								<label for="addRole">Role</label> <select name="role"
									id="addRole" required>
									<option value="customer">Customer</option>
									<option value="admin">Admin</option>
								</select>
							</div>
							<div>
								<label for="addPhoneNumber">Phone Number</label> <input
									type="text" name="phoneNumber" id="addPhoneNumber">
							</div>
							<div>
								<label for="addAddress">Address</label> <input type="text"
									name="address" id="addAddress">
							</div>
							<div>
								<label for="addCity">City</label> <input type="text" name="city"
									id="addCity">
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn-secondary"
								onclick="closeAddModal()">Close</button>
							<button type="submit" class="btn-primary">Create User</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script>
        const usersTable = document.getElementById('usersTable');
        const searchInput = document.getElementById('searchInput');
        const editModal = document.getElementById('editRoleModal');
        const addModal = document.getElementById('addUserModal');
        let timeout;

        // Add event listener for search input with debouncing
        searchInput.addEventListener('input', function(event) {
            clearTimeout(timeout);
            timeout = setTimeout(() => filterUsers(event.target.value), 300);
        });

        function filterUsers(query) {
            console.log(`Filtering users with query: ${query}`);
            if (!usersTable) {
                console.error('Users table not found');
                return;
            }
            const rows = usersTable.querySelectorAll('tbody tr');
            query = query.toLowerCase();
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

        function openAddModal() {
            document.getElementById('addUserForm').reset();
            addModal.style.display = 'flex';
        }

        function closeAddModal() {
            addModal.style.display = 'none';
        }
    </script>
</body>
</html>

