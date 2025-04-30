<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>About Us - SoraNet</title>
<style>
    /* Global Styles */
    .container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 16px;
    }

    /* Hero Section */
    .hero-section {
        position: relative;
    }
    
    .hero-image {
        width: 100%;
        height: 20rem;
        object-fit: cover;
        object-position: center;
        opacity: 0.75;
    }
    
    .hero-overlay {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        background-color: rgba(0, 0, 0, 0.6);
    }
    
    .hero-content {
        position: absolute;
        top: 33%;
        left: 17rem;
        color: white;
        display: flex;
        flex-direction: column;
    }
    
    .hero-title {
        font-size: 4rem;
        font-weight: 800;
        letter-spacing: -0.025em;
    }
    
    .hero-subtitle {
        font-size: 1.125rem;
        font-weight: 800;
        letter-spacing: -0.025em;
    }

    /* Our Story Section */
    .story-section {
        background-color: #f9fafb;
        padding: 5rem 1.5rem;
    }
    
    .story-container {
        max-width: 72rem;
        margin: 0 auto;
        text-align: center;
    }
    
    .section-heading {
        font-size: 1.875rem;
        font-weight: 700;
        margin-bottom: 3rem;
    }
    
    .content-wrapper {
        display: flex;
        flex-direction: column;
        gap: 3rem;
        align-items: flex-start;
    }
    
    .text-content {
        flex: 1;
        text-align: left;
    }
    
    .text-content p {
        font-size: 1.125rem;
        margin-bottom: 1.5rem;
    }
    
    .timeline {
        flex: 1;
        text-align: left;
    }
    
    .timeline-list {
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
    }
    
    .timeline-item {
        display: flex;
        align-items: flex-start;
    }
    
    .timeline-marker {
        color: #2563eb;
        margin-right: 0.75rem;
        font-size: 1.25rem;
    }
    
    .timeline-text {
        font-weight: 700;
    }

    @media (min-width: 768px) {
        .content-wrapper {
            flex-direction: row;
        }
    }
</style>
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