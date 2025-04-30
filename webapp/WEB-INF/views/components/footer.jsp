<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.footer {
	background: #1a202c;
	color: #a0aec0;
	padding: 40px 20px;
}

.footer-container {
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
	max-width: 1200px;
	margin: 0 auto;
}

.footer-section {
	flex: 1;
	min-width: 250px;
	margin: 20px 0;
}

.footer-section h3{
	color: white;
	margin-bottom: 10px;
}

.footer-logo {
	display: flex;
	align-items: center;
	font-size: 24px;
	margin-bottom: 10px;
}

.wifi-icon {
	margin-right: 10px;
}

.logo-textf {
	color: white;
	font-weight: bold;
	font-size: 28px;
}

.footer-description {
	font-size: 14px;
	line-height: 1.6;
	margin-bottom: 15px;
}

.social-links {
	margin-top: 10px;
}

.social-link {
	font-size: 20px;
	margin-right: 10px;
	color: #a0aec0;
	transition: color 0.3s ease;
}

.social-link:hover {
	color: #63b3ed;
}



.footer-links, .footer-contact {
	list-style: none;
}

.footer-links a {
	display: block;
	color: #a0aec0;
	margin-bottom: 8px;
	transition: color 0.3s ease;
}

.footer-links a:hover {
	color: #63b3ed;
}

.contact-item {
	display: flex;
	align-items: center;
	margin-bottom: 10px;
}

.contact-icon {
	margin-right: 10px;
	font-size: 18px;
}

@media ( max-width : 768px) {
	.footer-container {
		flex-direction: column;
		align-items: flex-start;
	}
}
</style>
</head>
<body>
	<footer class="footer">
		<div class="footer-container">
			<div class="footer-section">
				<div class="footer-logo">
					<span class="wifi-icon">📶</span> <span class="logo-textf">SoraNet</span>
				</div>
				<p class="footer-description">Providing lightning-fast internet
					services to homes and businesses since 2010.</p>
				<div class="social-links">
					<a href="#" class="social-link">🌐</a> <a href="#"
						class="social-link">🐦</a> <a href="#" class="social-link">📸</a>
				</div>
			</div>
			<div class="footer-section">
				<h3>Quick Links</h3>
				<ul class="footer-links">
					<li><a href="${pageContext.request.contextPath}/"
						class="nav-link">Home</a></li>
					<li><a href="${pageContext.request.contextPath}/plans"
						class="nav-link">Plans and Pricing</a></li>
					<li><a href="${pageContext.request.contextPath}/about"
						class="nav-link">About Us</a></li>
					<li><a href="${pageContext.request.contextPath}/contact"
						class="nav-link">Contact</a></li>
					<li><a href="${pageContext.request.contextPath}/reviews"
						class="nav-link">Reviews</a></li>
				</ul>
			</div>

			<div class="footer-section">
				<h3>Contact Us</h3>
				<ul class="footer-contact">
					<li class="contact-item"><span class="contact-icon">📍</span>
						<span>Lazimpat, Kathmandu</span></li>
					<li class="contact-item"><span class="contact-icon">📞</span>
						<span>(555) 123-4567</span></li>
					<li class="contact-item"><span class="contact-icon">✉️</span>
						<span>support@soranet.com</span></li>
				</ul>
			</div>
		</div>
	</footer>
</body>
</html>