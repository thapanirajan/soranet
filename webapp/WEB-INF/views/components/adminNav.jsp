<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SoraNet - Admin Dashboard</title>
    <style>
        * {
            padding: 0;
            margin: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
        }

        .container-dash {
            display: flex;
            min-height: 100vh;
        }

        .admin-nav {
            width: 250px;
            padding: 1rem 2rem;
            border-right: 1px solid black;
            background-color: #1f2937;
            color: white;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .admin-title {
            text-align: center;
        }

        .admin-title h2 {
            margin-bottom: 10px;
        }

        .admin-nav-links {
            margin-top: 20px;
            display: flex;
            flex-direction: column;
        }

        .admin-nav-links a {
            margin-top: 10px;
            text-decoration: none;
            border-radius: 5px;
            color: white;
            padding: 14px 1rem;
            transition: all 0.3s ease-in-out;
            border: 1px solid transparent;
        }

        .admin-nav-links a:hover {
            border: 1px solid white;
        }

        .admin-logout {
            padding: 10px 0;
            width: 100%;
            border-top: 1px solid white;
            text-align: center;
            margin-top: 2rem;
        }

        .admin-logout a {
            text-decoration: none;
            color: white;
        }

    </style>
</head>
<body>
    <div class="container-dash">
        <%-- Admin Nav --%>
        <div class="admin-nav">
            <div class="admin-title">
                <h2>SoraNet Admin</h2>
                <p>${sessionScope.user.getUsername()}</p>
            </div>
            <div class="admin-nav-links">
                <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/users">User Management</a>
                <a href="${pageContext.request.contextPath}/admin/plans">Plan Management</a>
                <a href="${pageContext.request.contextPath}/admin/subscriptions">Subscription Management</a>
                <a href="${pageContext.request.contextPath}/admin/payments">Payment Tracking</a>
                <a href="${pageContext.request.contextPath}/admin/profile">Profile</a>
            </div>
            <div class="admin-logout">
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </div>
    </div>
</body>
</html>
