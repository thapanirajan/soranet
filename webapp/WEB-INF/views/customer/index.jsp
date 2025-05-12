<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SoraNet - Home page</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/index.css">
<style>
.home-plan-grid {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	gap: 20px;
}

.home-plan-card {
	position: relative;
	width: 300px;
	background: white;
	border: 1px solid #ccc;
	border-radius: 12px;
	padding: 24px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	text-align: center;
	margin-bottom: 2rem;
}

.plan-badge {
	position: absolute;
	top: -12px;
	left: 50%;
	transform: translateX(-50%);
	background-color: #2962ff;
	color: white;
	padding: 5px 12px;
	font-size: 0.75rem;
	border-radius: 12px;
	font-weight: bold;
}

.home-plan-title {
	font-size: 1.3rem;
	margin-bottom: 10px;
}

.home-plan-price span {
	font-size: 1.8rem;
	font-weight: bold;
	color: #1a1a1a;
}

.home-plan-price small {
	font-size: 0.85rem;
	color: #666;
}

.home-plan-speed {
	margin: 12px auto;
	padding: 10px 0;
	width: 100%;
	background: #eef4ff;
	color: #2962ff;
	font-weight: bold;
	border-radius: 8px;
}

.home-plan-features {
	list-style: none;
	padding: 0;
	margin: 15px 0;
	text-align: left;
}

.home-plan-features li {
	margin: 6px 0;
}

.home-plan-btn {
	display: inline-block;
	padding: 10px 20px;
	margin-top: 10px;
	border-radius: 8px;
	font-weight: bold;
	text-decoration: none;
	transition: background-color 0.3s;
}

.btn-primary {
	background-color: #2962ff;
	color: white;
	border: none;
}

.btn-primary:hover {
	background-color: #0039cb;
}

.btn-outline {
	border: 1px solid #ccc;
	color: #333;
	background-color: white;
}

.btn-outline:hover {
	background-color: #f4f4f4;
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
					<img
						src="${pageContext.request.contextPath}/resources/images/herosection.jpg">
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
								<li>✔️ ${feature}</li>
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