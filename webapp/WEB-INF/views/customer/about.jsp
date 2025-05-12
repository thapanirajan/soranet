<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>About Us - SoraNet</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/about.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/components/header.jsp"%>
    
    <!-- Hero Section -->
    <section class="hero-section">
        <img class="hero-image" src="https://images.unsplash.com/photo-1541746972996-4e0b0f43e02a?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="">
        <div class="hero-overlay"></div>
        <div class="hero-content">
            <p class="hero-title">About Us</p>
            <p class="hero-subtitle">Learn about our company</p>
        </div>
    </section>

    <!-- Our Story Section -->
    <section id="story" class="story-section">
        <div class="story-container">
            <h2 class="section-heading">Our Journey</h2>
            <div class="content-wrapper">
                <div class="text-content">
                    <p>
                        Founded in 2020, SoraNet has been on a mission to revolutionize
                        connectivity. From humble beginnings to becoming a trusted name
                        across cities, our commitment remains the same: <em>to
                            deliver exceptional internet experiences for all.</em>
                    </p>
                </div>
                <div class="timeline">
                    <ul class="timeline-list">
                        <li class="timeline-item">
                            <span class="timeline-marker">✔️</span>
                            <p>
                                <span class="timeline-text">2020</span> - SoraNet was founded.
                            </p>
                        </li>
                        <li class="timeline-item">
                            <span class="timeline-marker">✔️</span>
                            <p>
                                <span class="timeline-text">2022</span> - Expanded to 10+ cities.
                            </p>
                        </li>
                        <li class="timeline-item">
                            <span class="timeline-marker">✔️</span>
                            <p>
                                <span class="timeline-text">2024</span> - 10,000+ active users
                                milestone.
                            </p>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </section>

    <%@ include file="/WEB-INF/views/components/footer.jsp"%>
</body>
</html>