<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<header>
	<div class="top-bar">Spedizione gratuita sopra i 50€</div>

	<div class="main-header">

		<div class="logo">
			<a href="${pageContext.request.contextPath}/"><img
				src="${pageContext.request.contextPath}/images/logo.jpg"></a>
		</div>

		<nav class="nav-menu">
			<ul>
				<li><a href="${pageContext.request.contextPath}/">Home</a></li>
				<li><a href="${pageContext.request.contextPath}/categoria?id=1">Trolley
						Rigidi</a></li>
				<li><a href="${pageContext.request.contextPath}/categoria?id=2">Trolley
						In Tessuto</a></li>
				<li><a href="${pageContext.request.contextPath}/categoria?id=3">Borsoni</a></li>
			</ul>
		</nav>

		<div class="header-icons">
			<a href="${pageContext.request.contextPath}/login"
				title="Area Utente"> <i class="fas fa-user"></i>
			</a> <a href="${pageContext.request.contextPath}/carrello"
				title="Carrello"> <i class="fas fa-shopping-cart"></i>
			</a> <i class="fas fa-bars hamburger"></i>
		</div>

	</div>
</header>