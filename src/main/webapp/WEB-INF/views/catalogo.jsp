<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${catNome}- Il Mio Ecommerce</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background-color: #f5f5f5;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
	background: white;
	padding: 30px;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
	color: #333;
	border-bottom: 2px solid #ff6b6b;
	padding-bottom: 10px;
}

.category-desc {
	color: #666;
	margin-bottom: 30px;
	padding: 15px;
	background-color: #f8f9fa;
	border-radius: 5px;
}

.products-grid {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
	gap: 20px;
	margin: 30px 0;
}

.product-card {
	border: 1px solid #ddd;
	border-radius: 10px;
	padding: 15px;
	text-align: center;
	transition: transform 0.3s, box-shadow 0.3s;
	background-color: white;
}

.product-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
}

.product-image {
	width: 100%;
	height: 200px;
	object-fit: cover;
	border-radius: 5px;
	margin-bottom: 10px;
}

.product-name {
	font-size: 18px;
	font-weight: bold;
	color: #333;
	margin: 10px 0;
}

.product-price {
	font-size: 20px;
	color: #ff6b6b;
	font-weight: bold;
	margin: 10px 0;
}

.product-stock {
	font-size: 12px;
	color: #666;
	margin: 5px 0;
}

.btn {
	display: inline-block;
	padding: 10px 20px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	font-size: 14px;
	border: none;
	cursor: pointer;
	margin-top: 10px;
}

.btn:hover {
	background-color: #ff5252;
}

.empty {
	text-align: center;
	padding: 50px;
	color: #666;
}

/* Paginazione */
.pagination {
	display: flex;
	justify-content: center;
	margin-top: 30px;
	gap: 5px;
	flex-wrap: wrap;
}

.pagination a, .pagination span {
	display: inline-block;
	padding: 8px 12px;
	text-decoration: none;
	border: 1px solid #ddd;
	border-radius: 5px;
	color: #333;
}

.pagination a:hover {
	background-color: #ff6b6b;
	color: white;
	border-color: #ff6b6b;
}

.pagination .active {
	background-color: #ff6b6b;
	color: white;
	border-color: #ff6b6b;
}

.info-pagina {
	text-align: center;
	margin-bottom: 20px;
	color: #666;
	font-size: 14px;
}

/* Ordinamento */
.sort-bar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 20px;
}

.sort-select {
	padding: 8px;
	border: 1px solid #ddd;
	border-radius: 5px;
	background-color: white;
	cursor: pointer;
}
</style>
</head>
<body>
	<div class="container">
		<h1>${catNome}</h1>

		<c:if test="${not empty catDesc}">
			<div class="category-desc">${catDesc}</div>
		</c:if>

		<!-- Barra ordinamento -->
		<div class="sort-bar">
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
						dal più basso</option>
					<option value="prezzo_desc"
						${param.ordine == 'prezzo_desc' ? 'selected' : ''}>Prezzo:
						dal più alto</option>
				</select>
			</form>
		</div>

		<c:choose>
			<c:when test="${empty prodotti}">
				<div class="empty">
					<p>Nessun prodotto disponibile in questa categoria.</p>
					<a href="${pageContext.request.contextPath}/home" class="btn">Torna
						alla home</a>
				</div>
			</c:when>
			<c:otherwise>
				<!-- Info paginazione -->
				<div class="info-pagina">
					<c:set var="paginaCorrente"
						value="${empty param.pagina ? 1 : param.pagina}" />
					Mostrando <strong>${prodotti.size()}</strong> di <strong>${totaleProdotti}</strong>
					prodotti
					<c:if test="${totalePagine > 1}">
                        - Pagina <strong>${paginaCorrente}</strong> di <strong>${totalePagine}</strong>
					</c:if>
				</div>

				<!-- Griglia prodotti -->
				<div class="products-grid">
					<c:forEach var="prodotto" items="${prodotti}">
						<div class="product-card">
							<!-- Immagine principale -->
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
                                        Disponibilità: ${prodotto.stock} pezzi
                                    </c:when>
									<c:otherwise>
										<span style="color: #dc3545;">Esaurito</span>
									</c:otherwise>
								</c:choose>
							</div>
							<a
								href="${pageContext.request.contextPath}/prodotto?id=${prodotto.id}"
								class="btn">Dettagli</a>
						</div>
					</c:forEach>
				</div>

				<!-- Paginazione -->
				<c:if test="${totalePagine > 1}">
					<div class="pagination">
						<c:choose>
							<c:when test="${paginaCorrente > 1}">
								<a
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=1&ordine=${param.ordine}">«
									Prima</a>
								<a
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=${paginaCorrente - 1}&ordine=${param.ordine}">‹
									Precedente</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">« Prima</span>
								<span class="disabled">‹ Precedente</span>
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
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=${paginaCorrente + 1}&ordine=${param.ordine}">Successivo
									›</a>
								<a
									href="${pageContext.request.contextPath}/categoria?id=${param.id}&pagina=${totalePagine}&ordine=${param.ordine}">Ultima
									»</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">Successivo ›</span>
								<span class="disabled">Ultima »</span>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
			</c:otherwise>
		</c:choose>

		<div style="margin-top: 20px;">
			<a href="${pageContext.request.contextPath}/home" class="back-link">←
				Torna alla home</a>
		</div>
	</div>
</body>
</html>