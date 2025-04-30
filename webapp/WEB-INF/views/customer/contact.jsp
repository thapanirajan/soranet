<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contact Us - SoraNet</title>
<style>
    /* Global Styles */
    body {
        margin: 0;
        padding: 0;
        font-family: Arial, sans-serif;
    }

    /* Hero Section */
    .hero-section {
        position: relative;
    }
    
    .hero-image {
        width: 100%;
        height: 25rem;
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
        margin-bottom: 1rem;
    }
    
    .hero-subtitle {
        font-size: 1.125rem;
        font-weight: 800;
        letter-spacing: -0.025em;
        max-width: 600px;
    }

    /* Contact Section */
    .contact-section {
        background-color: #f9fafb;
        padding: 5rem 0;
        margin: 5rem 12rem;
        display: flex;
        justify-content: space-around;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
    
    .contact-info {
        flex: 1;
        padding: 1rem;
        margin-left: 1rem;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }
    
    .contact-heading {
        font-size: 4rem;
        font-weight: 800;
        letter-spacing: -0.025em;
        margin-bottom: 1rem;
    }
    
    .contact-subheading {
        font-size: 1.125rem;
        letter-spacing: -0.025em;
        margin-bottom: 2rem;
    }
    
    .contact-details {
        font-size: 1rem;
        letter-spacing: -0.025em;
        margin-top: 1rem;
    }
    
    .contact-details div {
        margin: 0.5rem 0;
    }

    .contact-form {
        flex: 1;
        padding: 2rem;
        border: 1px solid #e5e7eb;
        border-radius: 0.75rem;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background: white;
    }
    
    .form-group {
        margin-bottom: 1.5rem;
    }
    
    label {
        display: block;
        margin-bottom: 0.5rem;
        font-size: 0.875rem;
        font-weight: 500;
    }
    
    input, textarea {
        width: 100%;
        padding: 0.75rem;
        border: 1px solid #d1d5db;
        border-radius: 0.5rem;
        font-size: 1rem;
    }
    
    input:focus, textarea:focus {
        outline: none;
        border-color: #2563eb;
        box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.2);
    }
    
    textarea {
        resize: vertical;
        min-height: 120px;
    }
    
    .submit-button {
        background-color: #2563eb;
        color: white;
        padding: 0.75rem 2rem;
        border: none;
        border-radius: 0.375rem;
        font-size: 1rem;
        cursor: pointer;
        letter-spacing: -0.025em;
    }
    
    .submit-button:hover {
        background-color: #1d4ed8;
    }

    @media (max-width: 768px) {
        .contact-section {
            flex-direction: column;
            margin: 2rem;
            padding: 2rem;
        }
        
        .hero-content {
            left: 2rem;
        }
        
        .hero-title {
            font-size: 2.5rem;
        }
    }
</style>
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