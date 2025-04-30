<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Plans and Pricing - SoraNet</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: Arial, Helvetica, sans-serif;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 0 16px;
}

.hero {
	background-color: #1d4ed8;
	color: white;
	padding: 64px 0;
	text-align: center;
}

.hero h1 {
	font-size: 2.25rem;
	font-weight: bold;
	margin-bottom: 16px;
}

.hero p {
	font-size: 1.25rem;
	max-width: 640px;
	margin: 0 auto;
	color: #bfdbfe;
}

.tabs {
	padding: 48px 0;
	text-align: center;
}

.tab-buttons {
	display: inline-flex;
	border-radius: 6px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	margin-bottom: 40px;
}

.tab-button {
	padding: 12px 24px;
	font-size: 0.875rem;
	font-weight: 500;
	border: none;
	cursor: pointer;
	transition: background-color 0.2s;
}

.tab-button.active {
	background-color: #2563eb;
	color: white;
	border-radius: 6px;
}

.tab-button.inactive {
	background-color: white;
	color: #374151;
	border: 1px solid #d1d5db;
}

.tab-button.inactive:hover {
	background-color: #f9fafb;
}

.plan-grid {
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
	gap: 32px;
	margin-bottom: 48px;
}

.plan-card {
	background: white;
	border: 1px solid rgba(0, 0, 0, 0.4);
	border-radius: 8px;
	padding: 24px;
	box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.3);
}

.plan-card.popular {
	border-color: #2563eb;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
	transform: scale(1.05);
}

.plan-card.popular::before {
	content: 'Most Popular';
	position: absolute;
	top: -12px;
	left: 50%;
	transform: translateX(-50%);
	background: #2563eb;
	color: white;
	padding: 4px 12px;
	border-radius: 12px;
	font-size: 0.75rem;
	font-weight: 500;
}

.plan-card h3 {
	font-size: 1.5rem;
	font-weight: 600;
	margin-bottom: 8px;
	text-align: left;
}

.plan-card .price {
	font-size: 2rem;
	font-weight: bold;
	color: #1f2937;
	margin-bottom: 8px;
	text-align: left;
}

.plan-card .speed {
	font-size: 1.125rem;
	color: #4b5563;
	margin-bottom: 16px;
	padding: 12px;
	background-color: #eff6ff;
	border-radius: 10px;
	color: #2563eb;
}

.plan-card ul {
	list-style: none;
	margin-bottom: 24px;
}

.plan-card li {
	display: flex;
	font-size: 0.99rem;
	color: #4b5563;
	margin-bottom: 8px;
}

.plan-card li::before {
	content: '✔';
	color: #16a34a;
	margin-right: 8px;
}

.btn {
	padding: 8px 16px;
	border-radius: 6px;
	text-decoration: none;
	font-weight: 500;
	display: inline-block;
}

.btn-primary {
	background-color: #2563eb;
	color: white;
	border: none;
}

.btn-primary:hover {
	background-color: #1d4ed8;
}

.btn-outline {
	border: 1px solid #d1d5db;
	color: #374151;
	background: none;
}

.btn-outline:hover {
	background-color: #f3f4f6;
}

.comparison {
	background-color: #f9fafb;
	padding: 48px 0;
}

.comparison h2 {
	font-size: 1.875rem;
	font-weight: bold;
	text-align: center;
	margin-bottom: 16px;
}

.comparison p {
	font-size: 1.125rem;
	color: #4b5563;
	text-align: center;
	max-width: 640px;
	margin: 0 auto 40px;
}

.table {
	width: 100%;
	border-collapse: collapse;
	background: white;
	border: 1px solid #e5e7eb;
}

.table th, .table td {
	padding: 12px;
	text-align: center;
	border-bottom: 1px solid #e5e7eb;
}

.table th {
	background: #f3f4f6;
	font-size: 0.75rem;
	text-transform: uppercase;
	color: #4b5563;
}

.table td {
	font-size: 0.875rem;
	color: #374151;
}

.table .popular {
	background: #eff6ff;
}

.table .popular span {
	color: #2563eb;
	font-size: 0.75rem;
} /* FAQ Section */
.faq {
	background-color: #f9f9f9;
	padding: 60px 20px;
	font-family: 'Arial', sans-serif;
}

.faq .container {
	max-width: 1000px;
	margin: 0 auto;
	text-align: center;
}

.faq h2 {
	font-size: 36px;
	margin-bottom: 10px;
	color: #333;
}

.faq p {
	font-size: 18px;
	color: #666;
	margin-bottom: 40px;
}

.faq-item {
	background: #fff;
	border: 1px solid #ddd;
	border-radius: 10px;
	padding: 20px 30px;
	margin-bottom: 20px;
	text-align: left;
	transition: box-shadow 0.3s ease;
}

.faq-item:hover {
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.faq-item h3 {
	font-size: 24px;
	margin-bottom: 10px;
}

.faq-item p {
	font-size: 16px;
	color: #555;
	line-height: 1.6;
}

@media ( max-width : 768px) {
	.hero h1 {
		font-size: 1.875rem;
	}
	.hero p {
		font-size: 1rem;
	}
	.plan-grid {
		grid-template-columns: 1fr;
	}
	.table {
		font-size: 0.75rem;
	}
}
</style>
<script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/components/header.jsp"%>
<!-- src="${pageContext.request.contextPath}/resources/images/plans.avif">  -->
	<!-- Hero Section -->
	<section class="relative mb-4">
		<img class="w-full h-[25rem] object-cover object-center opacity-xam75"
			src="https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
			alt="plan image">
		<div class="absolute inset-0 bg-black/60"></div>
		<div
			class="absolute inset-0 text-white flex flex-col top-1/3 left-[17rem]">
			<p class="text-[4rem] font-extrabold tracking-tight">Our Internet
				Plans</p>
			<p class="text-lg font-extrabold tracking-tight">Choose the
				perfect internet plan for your home or business. All plans <br>include
				free installation and no long-term contracts</p>
		</div>
	</section>
	
	<!-- Plan Selection Tabs -->
	<section class="tabs">
		<div class="container">
			<div class="tab-buttons">
				<button class="tab-button active" onclick="showTab('residential')">Residential
					Plans</button>
				<button class="tab-button inactive" onclick="showTab('business')">Business
					Plans</button>
			</div>
			<div id="residential" class="plan-grid">
				<c:forEach var="plan" items="${residentialPlans}">
					<div class="plan-card ${plan.popular ? 'popular' : ''}">
						<h3>${plan.planName}</h3>
						<div class="price">
							$${plan.price}<span style="opacity: 0.8; font-size: 1rem;">/month</span>
						</div>
						<div class="speed">${plan.speed}</div>
						<ul>
							<c:forEach var="feature" items="${plan.features}">
								<li>${feature}</li>
							</c:forEach>
						</ul>
						<a href="${pageContext.request.contextPath}/payment?planId=${plan.planId}"
							class="btn ${plan.popular ? 'btn-primary' : 'btn-outline'}">Select Plan</a>
					</div>
				</c:forEach>
			</div>
			<div id="business" class="plan-grid" style="display: none;">
				<c:forEach var="plan" items="${businessPlans}">
					<div class="plan-card ${plan.popular ? 'popular' : ''}">
						<h3>${plan.planName}</h3>
						<div class="price">
							$${plan.price}<span style="opacity: 0.8; font-size: 1rem;">/month</span>
						</div>
						<div class="speed">${plan.speed}</div>
						<ul>
							<c:forEach var="feature" items="${plan.features}">
								<li>${feature}</li>
							</c:forEach>
						</ul>
						<a href="${pageContext.request.contextPath}/payment?planId=${plan.planId}"
							class="btn ${plan.popular ? 'btn-primary' : 'btn-outline'}">Select
							Plan</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</section>

	<!-- Plan Comparison -->
	<section class="comparison">
		<div class="container">
			<h2>Plan Comparison</h2>
			<p>Compare our plans to find the perfect fit for your needs.</p>
			<div class="table-container">
				<div id="residential-table" class="table-wrapper">
					<table class="table">
						<thead>
							<tr>
								<th>Features</th>
								<c:forEach var="plan" items="${residentialPlans}">
									<th class="${plan.popular ? 'popular' : ''}">
										${plan.planName} <c:if test="${plan.popular}">
											<span>(Most Popular)</span>
										</c:if>
									</th>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>Download Speed</td>
								<c:forEach var="plan" items="${residentialPlans}">
									<td class="${plan.popular ? 'popular' : ''}">${plan.speed}</td>
								</c:forEach>
							</tr>
							<tr>
								<td>Upload Speed</td>
								<c:forEach var="plan" items="${residentialPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><c:choose>
											<c:when test="${plan.planName == 'Basic'}">Up to 20 Mbps</c:when>
											<c:when test="${plan.planName == 'Premium'}">Up to 50 Mbps</c:when>
											<c:when test="${plan.planName == 'Ultimate'}">Up to 100 Mbps</c:when>
											<c:otherwise>Unknown</c:otherwise>
										</c:choose></td>
								</c:forEach>
							</tr>
							<tr>
								<td>Data Cap</td>
								<c:forEach var="plan" items="${residentialPlans}">
									<td class="${plan.popular ? 'popular' : ''}">Unlimited</td>
								</c:forEach>
							</tr>
							<tr>
								<td>WiFi Router</td>
								<c:forEach var="plan" items="${residentialPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><c:choose>
											<c:when test="${plan.planName == 'Basic'}">Standard</c:when>
											<c:when test="${plan.planName == 'Premium'}">Dual-band</c:when>
											<c:when test="${plan.planName == 'Ultimate'}">Tri-band</c:when>
											<c:otherwise>Standard</c:otherwise>
										</c:choose></td>
								</c:forEach>
							</tr>
							<tr>
								<td>WiFi Extender</td>
								<c:forEach var="plan" items="${residentialPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><c:choose>
											<c:when test="${plan.planName == 'Ultimate'}">✔</c:when>
											<c:otherwise>—</c:otherwise>
										</c:choose></td>
								</c:forEach>
							</tr>
							<tr>
								<td>Price</td>
								<c:forEach var="plan" items="${residentialPlans}">
									<td class="${plan.popular ? 'popular' : ''}">$${plan.price}/mo</td>
								</c:forEach>
							</tr>
							<tr>
								<td></td>
								<c:forEach var="plan" items="${residentialPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><a
										href="${pageContext.request.contextPath}/payment?planId=${plan.planId}"
										class="btn ${plan.popular ? 'btn-primary' : 'btn-outline'}">Select
											Plan</a></td>
								</c:forEach>
							</tr>
						</tbody>
					</table>
				</div>
				<div id="business-table" class="table-wrapper"
					style="display: none;">
					<table class="table">
						<thead>
							<tr>
								<th>Features</th>
								<c:forEach var="plan" items="${businessPlans}">
									<th class="${plan.popular ? 'popular' : ''}">
										${plan.planName} <c:if test="${plan.popular}">
											<span>(Most Popular)</span>
										</c:if>
									</th>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>Download Speed</td>
								<c:forEach var="plan" items="${businessPlans}">
									<td class="${plan.popular ? 'popular' : ''}">${plan.speed}</td>
								</c:forEach>
							</tr>
							<tr>
								<td>Upload Speed</td>
								<c:forEach var="plan" items="${businessPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><c:choose>
											<c:when test="${plan.planName == 'Business Starter'}">Up to 50 Mbps</c:when>
											<c:when test="${plan.planName == 'Business Pro'}">Up to 100 Mbps</c:when>
											<c:when test="${plan.planName == 'Business Enterprise'}">Up to 200 Mbps</c:when>
											<c:otherwise>Unknown</c:otherwise>
										</c:choose></td>
								</c:forEach>
							</tr>
							<tr>
								<td>Data Cap</td>
								<c:forEach var="plan" items="${businessPlans}">
									<td class="${plan.popular ? 'popular' : ''}">Unlimited</td>
								</c:forEach>
							</tr>
							<tr>
								<td>Router Type</td>
								<c:forEach var="plan" items="${businessPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><c:choose>
											<c:when test="${plan.planName == 'Business Starter'}">Business-grade</c:when>
											<c:when test="${plan.planName == 'Business Pro'}">Business-grade</c:when>
											<c:when test="${plan.planName == 'Business Enterprise'}">Enterprise-grade</c:when>
											<c:otherwise>Standard</c:otherwise>
										</c:choose></td>
								</c:forEach>
							</tr>
							<tr>
								<td>Static IPs</td>
								<c:forEach var="plan" items="${businessPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><c:choose>
											<c:when test="${plan.planName == 'Business Starter'}">1</c:when>
											<c:when test="${plan.planName == 'Business Pro'}">Multiple</c:when>
											<c:when test="${plan.planName == 'Business Enterprise'}">Multiple</c:when>
											<c:otherwise>—</c:otherwise>
										</c:choose></td>
								</c:forEach>
							</tr>
							<tr>
								<td>Price</td>
								<c:forEach var="plan" items="${businessPlans}">
									<td class="${plan.popular ? 'popular' : ''}">$${plan.price}/mo</td>
								</c:forEach>
							</tr>
							<tr>
								<td></td>
								<c:forEach var="plan" items="${businessPlans}">
									<td class="${plan.popular ? 'popular' : ''}"><a
										href="${pageContext.request.contextPath}/payment?planId=${plan.planId}"
										class="btn ${plan.popular ? 'btn-primary' : 'btn-outline'}">Select
											Plan</a></td>
								</c:forEach>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</section>

	<!-- FAQ Section -->
	<section class="faq">
		<div class="container">
			<h2>Frequently Asked Questions</h2>
			<p>Find answers to common questions about our internet plans and
				services.</p>
			<div class="faq-item">
				<h3>Are there any installation fees?</h3>
				<p>No, all our plans come with free standard installation. Our
					technician will set up your equipment and ensure your connection is
					working properly.</p>
			</div>
			<div class="faq-item">
				<h3>Do I need to sign a contract?</h3>
				<p>No, all our residential plans are contract-free. You can
					cancel anytime without early termination fees. Business plans have
					flexible contract options.</p>
			</div>
			<div class="faq-item">
				<h3>What equipment is included?</h3>
				<p>All plans include a WiFi router appropriate for the speed
					tier. The Ultimate plan also includes a WiFi extender for larger
					homes. Business plans include business-grade networking equipment.</p>
			</div>
			<div class="faq-item">
				<h3>Can I upgrade my plan later?</h3>
				<p>Yes, you can upgrade or downgrade your plan at any time.
					Changes typically take effect on your next billing cycle. There's
					no fee for changing plans.</p>
			</div>
		</div>
	</section>


	<%@ include file="/WEB-INF/views/components/footer.jsp"%>

	<script>
        function showTab(type) {
            // Toggle plan grids
            document.getElementById('residential').style.display = type === 'residential' ? 'grid' : 'none';
            document.getElementById('business').style.display = type === 'business' ? 'grid' : 'none';
            // Toggle comparison tables
            document.getElementById('residential-table').style.display = type === 'residential' ? 'block' : 'none';
            document.getElementById('business-table').style.display = type === 'business' ? 'block' : 'none';
            // Update tab button styles
            document.querySelectorAll('.tab-button').forEach(btn => {
                btn.classList.remove('active');
                btn.classList.add('inactive');
                if (btn.textContent.includes(type.charAt(0).toUpperCase() + type.slice(1))) {
                    btn.classList.add('active');
                    btn.classList.remove('inactive');
                }
            });
        }
    </script>
</body>
</html>