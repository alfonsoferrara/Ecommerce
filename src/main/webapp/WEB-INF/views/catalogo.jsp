<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${catNome}- CarryCrew</title>
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/categoria.css">
</head>
<body>

	<jsp:include page="fragments/header.jsp" />

	<main class="category-container">
		<h1 class="category-title">${catNome}</h1>

		<c:if test="${not empty catDesc}">
			<div class="category-desc">${catDesc}</div>
		</c:if>

		<c:choose>
			<c:when test="${empty prodotti}">
				<div class="empty-state">
					<p>Nessun prodotto disponibile in questa categoria al momento.</p>
					<a href="${pageContext.request.contextPath}/home"
						class="btn-primary">Torna alla home</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="sort-section">
					<div class="info-pagina">
						<c:set var="paginaCorrente"
							value="${empty param.pagina ? 1 : param.pagina}" />
						Mostrando <strong>${prodotti.size()}</strong> di <strong>${totaleProdotti}</strong>
						prodotti
						<c:if test="${totalePagine > 1}">
                            - Pagina <strong>${paginaCorrente}</strong> di <strong>${totalePagine}</strong>
						</c:if>
					</div>

					<!-- param.ordine viene dai parametri della query string nell'URL -->
					<form action="${pageContext.request.contextPath}/categoria"
						method="GET" id="sortForm">
						<input type="hidden" name="id" value="${param.id}"> <input
							type="hidden" name="pagina" value="1"> <select
							name="ordine" class="sort-select" onchange="this.form.submit()">
							<option value="recenti"
								${param.ordine == 'recenti' or empty param.ordine ? 'selected' : ''}>Più
								recenti</option>
							<option value="prezzo_asc"
								${param.ordine == 'prezzo_asc' ? 'selected' : ''}>Prezzo:
								Crescente</option>
							<option value="prezzo_desc"
								${param.ordine == 'prezzo_desc' ? 'selected' : ''}>Prezzo:
								Decrescente</option>
						</select>
					</form>
				</div>

				<div class="products-grid">
					<c:forEach var="prodotto" items="${prodotti}">
						<div class="product-card">

							<c:set var="imgUrl" value="${immaginiPrincipali[prodotto.id]}" />
							<c:choose>
								<c:when test="${not empty imgUrl}">
									<img src="${pageContext.request.contextPath}${imgUrl}"
										alt="${prodotto.nome}" class="product-image"
										onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
								</c:when>
								<c:otherwise>
									<img
										src="${pageContext.request.contextPath}/images/default.jpg"
										alt="${prodotto.nome}" class="product-image">
								</c:otherwise>
							</c:choose>

							<div class="product-name">${prodotto.nome}</div>
							<div class="product-price">€ ${String.format("%.2f", prodotto.prezzo)}</div>

							<div class="product-stock">
								<c:choose>
									<c:when test="${prodotto.stock > 0}">
                                        Disponibilità: ${prodotto.stock} pz.
                                    </c:when>
									<c:otherwise>
										<span class="stock-out">Esaurito</span>
									</c:otherwise>
								</c:choose>
							</div>

							<a
								href="${pageContext.request.contextPath}/prodotto?id=${prodotto.id}"
								class="btn-outline">Dettagli</a>
						</div>
					</c:forEach>
				</div>

				<c:if test="${totalePagine > 1}">
					<div class="pagination">
						<c:choose>
							<c:when test="${paginaCorrente > 1}">
								<a
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=1&ordine=${param.ordine}">&laquo;
									Prima</a>
								<a
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=${paginaCorrente - 1}&ordine=${param.ordine}">&lsaquo;
									Prec</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">&laquo; Prima</span>
								<span class="disabled">&lsaquo; Prec</span>
							</c:otherwise>
						</c:choose>

						<c:set var="inizio" value="${paginaCorrente - 2}" />
						<c:set var="fine" value="${paginaCorrente + 2}" />
						<c:if test="${inizio < 1}">
							<c:set var="inizio" value="1" />
						</c:if>
						<c:if test="${fine > totalePagine}">
							<c:set var="fine" value="${totalePagine}" />
						</c:if>

						<c:forEach var="i" begin="${inizio}" end="${fine}">
							<c:choose>
								<c:when test="${i == paginaCorrente}">
									<span class="active">${i}</span>
								</c:when>
								<c:otherwise>
									<a
										href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=${i}&ordine=${param.ordine}">${i}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:choose>
							<c:when test="${paginaCorrente < totalePagine}">
								<a
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=${paginaCorrente + 1}&ordine=${param.ordine}">Succ
									&rsaquo;</a>
								<a
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=${totalePagine}&ordine=${param.ordine}">Ultima
									&raquo;</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">Succ &rsaquo;</span>
								<span class="disabled">Ultima &raquo;</span>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
			</c:otherwise>
		</c:choose>

		<a href="${pageContext.request.contextPath}/home" class="back-link">&larr;
			Torna alla home</a>
	</main>

	<jsp:include page="fragments/footer.jsp" />

	<script src="${pageContext.request.contextPath}/scripts/main.js"></script>

</body>
</html>