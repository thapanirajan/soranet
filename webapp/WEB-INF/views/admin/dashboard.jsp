<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoraNet - Admin Dashboard</title>
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

        .dash-container {
            display: flex;
            min-height: 100vh;
        }

        .dashboard-head {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            z-index: 1000;
        }

        .dashboard-container {
            flex-grow: 1;
            padding: 2rem;
            margin-left: 250px; /* Adjust based on your adminNav.jsp sidebar width */
            margin-top: 60px; /* Adjust based on header height */
            overflow-y: auto;
            height: 150vh;
        }

        .dashboard-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1rem;
        }

        .card {
            background-color: #f3f4f6;
            padding: 1rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .card h5 {
            margin: 0;
            font-size: 1rem;
            color: #1f2937;
        }

        .card h6 {
            margin: 0;
            font-size: 1.2rem;
            color: #1f2937;
        }

        .card span {
            font-size: 1.5rem;
        }

        @media (max-width: 768px) {
            .dashboard-container {
                margin-left: 0;
                padding: 1rem;
                margin-top: 100px; /* Adjust for header height on mobile */
            }

            .dashboard-head {
                position: fixed;
                width: 100%;
            }

            .card {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="dash-container">
        <div class="dashboard-head">
            <%@ include file="/WEB-INF/views/components/adminNav.jsp"%>
        </div>
        <div class="dashboard-container">
            <h2>Dashboard</h2>
            <div class="dashboard-cards">
                <div class="card">
                    <span>👤</span>
                    <div>
                        <h5>Total Users</h5>
                        <h6>${dashboardData.totalUsers}</h6>
                    </div>
                </div>
                <div class="card">
                    <span>📋</span>
                    <div>
                        <h5>Active Subscriptions</h5>
                        <h6>${dashboardData.activeSubs}</h6>
                    </div>
                </div>
                <div class="card">
                    <span>⏰</span>
                    <div>
                        <h5>Expiring Subscriptions</h5>
                        <h6>${dashboardData.expiringSubs}</h6>
                    </div>
                </div>
                <div class="card">
                    <span>💰</span>
                    <div>
                        <h5>Total Revenue</h5>
                        <h6>${dashboardData.totalRevenue}</h6>
                    </div>
                </div>
                <div class="card">
                    <span>📈</span>
                    <div>
                        <h5>Monthly Revenue</h5>
                        <h6>${dashboardData.monthlyRevenue}</h6>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>