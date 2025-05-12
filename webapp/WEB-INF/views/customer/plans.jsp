<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Plans and Pricing - SoraNet</title>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/plans.css">
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