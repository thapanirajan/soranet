<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contact Us - SoraNet</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/contact.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/components/header.jsp"%>

    <!-- Hero Section -->
    <section class="hero-section">
        <img class="hero-image" src="https://images.unsplash.com/photo-1541746972996-4e0b0f43e02a?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D">
        <div class="hero-overlay"></div>
        <div class="hero-content">
            <p class="hero-title">Get in touch</p>
            <p class="hero-subtitle">We’d love to hear from you! Please fill out the form below or reach out using our contact information</p>
        </div>
    </section>

    <!-- Contact Form Section -->
    <section class="contact-section">
        <div class="contact-info">
            <div>
                <div class="contact-heading">Lets talk</div>
                <div class="contact-subheading">Ask us anything or just say hi...</div>
            </div>
            <div class="contact-details">
                <div>📞 1 234 567 890</div>
                <div>✉️ soranet@gmail.com</div>
                <div>Lazimpat, Kathmandu</div>
            </div>
        </div>
        <div class="contact-form">
            <form onsubmit="handleSubmit()">
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" name="name" placeholder="Your Name" required>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" placeholder="Your Email" required>
                </div>
                <div class="form-group">
                    <label for="message">Message</label>
                    <textarea id="message" name="message" rows="5" placeholder="Write your message..." required></textarea>
                </div>
                <div>
                    <button type="submit" class="submit-button">Send Message</button>
                </div>
            </form>
        </div>
    </section>

    <%@ include file="/WEB-INF/views/components/footer.jsp"%>
    <script>
        const handleSubmit = () => {
            alert("Form submitted ");
        }
    </script>
</body>
</html>