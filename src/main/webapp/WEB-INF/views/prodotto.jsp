<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${prodotto.nome}|CarryCrew</title>
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/prodotto.css">
</head>
<body>

	<jsp:include page="fragments/header.jsp" />

	<main class="product-detail-container">

		<div class="product-gallery">
			<c:choose>
				<c:when test="${not empty immagini}">
					<c:forEach var="immagine" items="${immagini}">
						<img src="${pageContext.request.contextPath}/${immagine.url}"
							alt="${prodotto.nome}" onerror="this.style.display='none'">
					</c:forEach>
				</c:when>
				<c:otherwise>
					<img src="${pageContext.request.contextPath}/images/default.jpg"
						alt="Nessuna immagine disponibile">
				</c:otherwise>
			</c:choose>
		</div>

		<div class="product-info-wrapper">

			<h1 class="product-detail-title">${prodotto.nome}</h1>
			<div class="product-detail-price">€ ${String.format("%.2f", prodotto.prezzo)}</div>

			<c:if test="${not empty specifiche}">
				<div class="product-specs">
					<h3>Dettagli</h3>
					<div class="spec-item">
						<strong>Quantità disponibile: </strong> <span>${prodotto.stock}</span>
					</div>
					<c:forEach var="entry" items="${specifiche}">
						<div class="spec-item">
							<strong>${entry.key}</strong> <span>${entry.value}</span>
						</div>
					</c:forEach>
				</div>
			</c:if>

			<form action="${pageContext.request.contextPath}/carrello"
				method="POST" class="add-to-cart-form">
				<input type="hidden" name="action" value="add"> <input
					type="hidden" name="prodottoId" value="${prodotto.id}"> <input
					type="number" name="quantita" value="1" min="1" max="99"
					class="quantity-input" title="Quantità">
				<c:if test="${prodotto.stock > 0}">
					<button type="submit" class="btn-add-cart">Aggiungi al
						carrello</button>
				</c:if>
				<c:if test="${prodotto.stock <= 0}">
					<button type="" class="btn-add-cart" style="pointer-events: none; opacity: 0.5;" disabled>Prodotto esaurito</button>
				</c:if>
			</form>
			<p id="aggiunta_al_carrello" style="display: none">Prodotto
				aggiunto al carrello!</p>

			<a href="${pageContext.request.contextPath}/carrello"
				class="link-cart"> <i class="fas fa-shopping-cart"></i> Vai al
				carrello
			</a>

			<c:if test="${not empty prodotto.descrizione}">
				<div class="product-description">
					<h3>Descrizione</h3>
					<p>${prodotto.descrizione}</p>
				</div>
			</c:if>

		</div>

	</main>

	<jsp:include page="fragments/footer.jsp" />

	<script src="${pageContext.request.contextPath}/scripts/main.js"></script>

</body>
</html>