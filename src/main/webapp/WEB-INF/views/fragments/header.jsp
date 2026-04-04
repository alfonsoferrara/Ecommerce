<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

<!-- Top bar - Spedizione gratuita -->
<div class="top-bar">🚚 SPEDIZIONE GRATUITA PER ORDINI SUPERIORI A
	50€</div>

<!-- Header principale -->
<header class="main-header">
	<div class="header-content">
		<!-- Logo a sinistra -->
		<div class="logo">
			<a href="${pageContext.request.contextPath}/home"> <span>✦</span>
				SHOP<span>.</span>
			</a>
		</div>

		<!-- Pulsante menu mobile -->
		<button class="menu-toggle" id="menuToggle">
			<i class="fas fa-bars"></i>
		</button>

		<!-- Menu al centro -->
		<nav class="main-nav" id="mainNav">
			<ul class="nav-menu">
				<li><a href="${pageContext.request.contextPath}/home"><i
						class="fas fa-home"></i> Home</a></li>
				<li><a href="${pageContext.request.contextPath}/catalogo"><i
						class="fas fa-tags"></i> Catalogo</a></li>
				<li><a href="${pageContext.request.contextPath}/offerte"><i
						class="fas fa-percent"></i> Offerte</a></li>
				<li><a href="${pageContext.request.contextPath}/contatti"><i
						class="fas fa-envelope"></i> Contatti</a></li>
			</ul>
		</nav>

		<!-- Icone a destra -->
		<div class="user-actions">
			<c:choose>
				<c:when test="${not empty sessionScope.utenteLoggato}">
					<a href="${pageContext.request.contextPath}/user/profilo"
						title="Il mio profilo"> <i class="fas fa-user-circle"></i>
					</a>
					<a href="${pageContext.request.contextPath}/logout" title="Logout">
						<i class="fas fa-sign-out-alt"></i>
					</a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/login" title="Accedi">
						<i class="fas fa-user"></i>
					</a>
				</c:otherwise>
			</c:choose>
			<a href="${pageContext.request.contextPath}/carrello"
				class="cart-link" title="Carrello"> <i
				class="fas fa-shopping-bag"></i> <span class="cart-count"
				id="cartCount"> <c:set var="cartSize"
						value="${sessionScope.cartSize}" /> <c:if
						test="${not empty cartSize and cartSize > 0}">${cartSize}</c:if>
			</span>
			</a>
		</div>
	</div>
</header>

<script src="${pageContext.request.contextPath}/scripts/main.js"></script>