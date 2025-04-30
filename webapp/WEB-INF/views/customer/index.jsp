<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SoraNet - Home page</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/index.css" type="text/css">
<style>
body {
	font-family: Arial, sans-serif;
	line-height: 1.6;
	background-color: #fff;
}

.hero {
	background: linear-gradient(to bottom right, #1e40af, #1d4ed8);
	color: white;
	padding: 5rem 0;
}

.hero-content {
	display: flex;
	gap: 2rem;
	max-width: 77%;
	margin: 0 auto;
	align-items: center;
}

.hero-text {
	flex: 1;
}

.hero-text h1 {
	font-size: 2.5rem;
	margin-bottom: 1.5rem;
}

.hero-text p {
	font-size: 1.25rem;
	margin-bottom: 2rem;
	color: #bfdbfe;
}

.hero-image {
	flex: 1;
}

.hero-image img {
	width: 100%;
	border-radius: 0.5rem;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.buttons {
	display: flex;
	gap: 1rem;
}

.button {
	padding: 1rem 2rem;
	border-radius: 0.375rem;
	font-weight: bold;
	text-decoration: none;
	transition: background-color 0.3s, color 0.3s;
}

.button.primary {
	background-color: #3b82f6;
	color: white;
	border: 2px solid #3b82f6;
}

.button.primary:hover {
	background-color: #2563eb;
	border-color: #2563eb;
}

.button.outline {
	background-color: transparent;
	color: white;
	border: 2px solid white;
}

.button.outline:hover {
	background-color: white;
	color: #3b82f6;
}

.features, .testimonials {
	background-color: #f9fafb;
	padding: 4rem 0;
}

.features-header, .testimonials-header, .plans-header {
	text-align: center;
	margin-bottom: 3rem;
}

.features-header h2, .testimonials-header h2, .plans-header h2 {
	font-size: 2rem;
	margin-bottom: 1rem;
}

.features-header p, .testimonials-header p, .plans-header p {
	font-size: 1.125rem;
	color: #6b7280;
	max-width: 36rem;
	margin: 0 auto;
}

.features-grid, .testimonials-grid {
	display: grid;
	gap: 2rem;
	max-width: 1200px;
	margin: 0 auto;
}

.plans {
	margin: 4px;
}

.plans-grid {
	display: flex;
}

.home-plan-card {
	background: white;
	padding: 30px;
	border-radius: 12px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	width: 300px;
	text-align: center;
}

.home-plan-title {
	margin: 0;
	font-size: 24px;
}

.home-plan-price {
	font-size: 24px;
	margin: 15px 0;
}

.home-plan-price span {
	font-weight: bold;
}

.home-plan-price small {
	font-size: 14px;
	color: gray;
}

.home-plan-speed {
	background: #f0f6ff;
	color: #005bff;
	font-weight: bold;
	padding: 10px;
	margin: 15px 0;
	border-radius: 10px;
}

.home-plan-features {
	list-style: none;
	padding: 0;
	text-align: left;
	margin-bottom: 20px;
}

.home-plan-features li {
	margin: 10px 0;
	color: #333;
}

.home-plan-btn {
	padding: 10px 20px;
	border: 1px solid #ccc;
	background: white;
	border-radius: 6px;
	cursor: pointer;
	transition: 0.3s;
}

.home-plan-btn:hover {
	background: #f0f0f0;
}

.features-grid {
	grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
}

.feature-card {
	background-color: white;
	padding: 1.5rem;
	border-radius: 0.5rem;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	text-align: center;
}

.feature-card .icon {
	width: 4rem;
	height: 4rem;
	border-radius: 9999px;
	margin: 0 auto 1rem;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 1.5rem;
}

.feature-card h3 {
	font-size: 1.25rem;
	margin-bottom: 0.5rem;
}

.feature-card p {
	color: #6b7280;
}

.home-plan-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 20px;
	justify-content: center;
	padding: 20px;
}

.home-plan-card {
	background-color: #fff;
	padding: 30px;
	width: 300px;
	border-radius: 10px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	text-align: center;
}

.home-plan-title {
	font-size: 1.5rem;
	font-weight: bold;
	margin-bottom: 10px;
}

.home-plan-price {
	font-size: 1.8rem;
	font-weight: bold;
	margin-bottom: 10px;
}

.home-plan-price small {
	font-size: 1rem;
	opacity: 0.8;
}

.home-plan-speed {
	background-color: #eef5ff;
	color: #0071f2;
	font-weight: bold;
	padding: 10px;
	border-radius: 8px;
	margin: 15px 0;
}

.home-plan-features {
	list-style: none;
	padding: 0;
	text-align: left;
	margin: 20px 0;
}

.home-plan-features li {
	margin-bottom: 8px;
	position: relative;
	padding-left: 20px;
	color: #333;
}

.home-plan-features li::before {
	content: "✔";
	color: green;
	position: absolute;
	left: 0;
}

.home-plan-btn {
	display: inline-block;
	padding: 10px 20px;
	border-radius: 8px;
	font-weight: 500;
	text-decoration: none;
	transition: background-color 0.3s ease;
}

.btn-primary {
	background-color: #0071f2;
	color: white;
	border: none;
}

.btn-outline {
	background-color: white;
	color: #0071f2;
	border: 1px solid #0071f2;
}
</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/components/header.jsp"%>
	<main>
		<!-- Hero Section -->
		<section class="hero">
			<div class="hero-content">
				<div class="hero-text">
					<h1>
						Lightning Fast Internet for <br> Your Home and Business
					</h1>
					<p>
						Experience blazing speeds, reliable connections, and exceptional <br>
						service with SoraNet - your trusted local internet service <br>
						provider.
					</p>
					<div class="buttons">
						<a href="${pageContext.request.contextPath}/plan"
							class="button primary">View Plans</a> <a
							href="${pageContext.request.contextPath}/contact"
							class="button outline">Contact Sales</a>
					</div>
				</div>

				<div class="hero-image">
					<%-- <img src="${pageContext.request.contextPath}/resources/images/herosection.jpg"> --%>
					<img
						src="https://images.unsplash.com/photo-1640622304964-3e2c2c0cd7cd?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D">
				</div>
			</div>
		</section>

		<!-- Features Section -->
		<section class="features">
			<div class="features-header">
				<h2>Why Choose SoraNet?</h2>
				<p>We deliver more than just internet. Experience the SoraNet
					difference with our premium features and benefits.</p>
			</div>
			<div class="features-grid">
				<div class="feature-card">
					<div class="icon" style="background-color: #bfdbfe;">⚡</div>
					<h3>High-Speed</h3>
					<p>Blazing fast speeds up to 1 Gbps for seamless streaming and
						gaming.</p>
				</div>
				<div class="feature-card">
					<div class="icon" style="background-color: #bfdbfe;">📶</div>
					<h3>Reliability</h3>
					<p>99.9% uptime guarantee with redundant network
						infrastructure.</p>
				</div>
				<div class="feature-card">
					<div class="icon" style="background-color: #bfdbfe;">🛡️</div>
					<h3>Security</h3>
					<p>Advanced security features to protect your network and
						devices.</p>
				</div>
				<div class="feature-card">
					<div class="icon" style="background-color: #bfdbfe;">📞</div>
					<h3>24/7 Support</h3>
					<p>Local, friendly support team available around the clock.</p>
				</div>
			</div>
		</section>

		<!-- Featured Plans Section -->
		<section class="plans">
			<div class="plans-header">
				<h2>Our Most Popular Plans</h2>
				<p>Choose the perfect internet plan for your needs and budget.
					All plans include free installation and no contracts.</p>
			</div>
			<div id="residential" class="home-plan-grid">
				<c:forEach var="plan" items="${residentialPlans}">
					<div class="home-plan-card">
						<h3 class="home-plan-title">${plan.planName}</h3>
						<div class="home-plan-price">
							<span>$${plan.price}</span> <small>/month</small>
						</div>
						<div class="home-plan-speed">${plan.speed}</div>
						<ul class="home-plan-features">
							<c:forEach var="feature" items="${plan.features}">
								<li>${feature}</li>
							</c:forEach>
						</ul>
						<a href="${pageContext.request.contextPath}/plan"
							class="home-plan-btn ${plan.popular ? 'btn-primary' : 'btn-outline'}">
							Select Plan </a>
					</div>
				</c:forEach>
			</div>
		</section>

	</main>
	<div class="home-container"></div>
	<%@ include file="/WEB-INF/views/components/footer.jsp"%>
</body>
</html>