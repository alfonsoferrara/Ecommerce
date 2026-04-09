<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
				<c:forEach var="categoria" items="${categorie}">
					<li><a
						href="${pageContext.request.contextPath}/categoria?id=${categoria.id}">
							${categoria.nome} </a></li>
				</c:forEach>
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