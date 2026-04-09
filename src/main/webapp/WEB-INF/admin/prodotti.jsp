<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestione Prodotti | CarryCrew Admin</title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-dashboard.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-sidebar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-topbar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-prodotti.css">
</head>
<body>

	<jsp:include page="/WEB-INF/admin/fragments/admin-sidebar.jsp" />

	<main class="admin-main-content">

		<jsp:include page="/WEB-INF/admin/fragments/admin-topbar.jsp" />

		<div class="dashboard-content">

			<c:if test="${not empty errore}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${errore}
				</div>
			</c:if>

			<div class="filter-panel">
				<h3>Filtra Prodotti</h3>
				<form action="${pageContext.request.contextPath}/admin/prodotti"
					method="GET" class="filter-form">

					<div class="filter-group">
						<label for="ordinamentoSelect">Cerca per:</label> <select
							name="ordinamento" id="ordinamentoSelect">
							<option value="recenti"
								${param.ordinamento == 'recenti' ? 'selected' : ''}>Ultimi
								Inseriti</option>
							<option value="prodottoId"
								${param.ordinamento == 'prodottoId' ? 'selected' : ''}>ID
								Prodotto</option>
							<option value="categoriaId"
								${param.ordinamento == 'categoriaId' ? 'selected' : ''}>Nome
								Categoria</option>
							<option value="stato"
								${param.ordinamento == 'stato' ? 'selected' : ''}>Stato
								(Attivo/Disattivo)</option>
							<option value="stock"
								${param.ordinamento == 'stock' ? 'selected' : ''}>Range
								di Stock</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-prodotto">
						<label>ID Prodotto</label> <input type="number" name="prodotto_id"
							value="${param.prodotto_id}" placeholder="Es. 101">
					</div>

					<div class="filter-group filter-hidden" id="group-categoria">
						<label>Categoria</label> <select name="categoria_id">
							<option value="">Seleziona una categoria</option>
							<c:forEach items="${tutteCategorie}" var="categoria">
								<option value="${categoria.nome}"
									${param.categoria_id == categoria.nome ? 'selected' : ''}>
									${categoria.nome}</option>
							</c:forEach>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-stato">
						<label>Stato Prodotto</label> <select name="stato_prodotto">
							<option value="1"
								${param.stato_prodotto == '1' ? 'selected' : ''}>Attivo
								(Visibile)</option>
							<option value="0"
								${param.stato_prodotto == '0' ? 'selected' : ''}>Disattivo
								(Soft Delete)</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-stock-min">
						<label>Stock Minimo</label> <input type="number" name="minimo"
							value="${param.minimo}" min="0">
					</div>

					<div class="filter-group filter-hidden" id="group-stock-max">
						<label>Stock Massimo</label> <input type="number" name="massimo"
							value="${param.massimo}" min="0">
					</div>

					<div class="filter-group">
						<button type="submit" class="btn-filter">Applica Filtro</button>
					</div>
					<div class="filter-group">
						<button type="button" class="btn-filter"
							onclick="window.location.href='${pageContext.request.contextPath}/admin/prodotti'">Reset</button>
					</div>
				</form>
			</div>

			<section class="dashboard-section">
				<div class="section-title-wrapper">
					<h2>Catalogo Prodotti</h2>
					<a href="${pageContext.request.contextPath}/admin/prodotto?id=new"
						class="btn-add"> <i class="fas fa-plus"></i> Nuovo Prodotto
					</a>
				</div>
				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID</th>
								<th>Categoria</th>
								<th>Nome Prodotto</th>
								<th>Prezzo</th>
								<th>Stock</th>
								<th>Stato</th>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${prodotti}" var="prodotto" varStatus="loop">
								<tr>
									<td><strong>#${prodotto.id}</strong></td>

									<td>${nomiCategorie[loop.index]}</td>

									<td><strong>${prodotto.nome}</strong></td>
									<td><fmt:formatNumber value="${prodotto.prezzo}"
											type="currency" currencySymbol="€" /></td>

									<td
										style="${prodotto.stock <= 0 ? 'color: #e74c3c; font-weight: bold;' : ''}">
										${prodotto.stock} pz.</td>

									<td><c:choose>
											<c:when test="${prodotto.attivo}">
												<span class="status-badge badge-active">Attivo</span>
											</c:when>
											<c:otherwise>
												<span class="status-badge badge-inactive">Disattivo</span>
											</c:otherwise>
										</c:choose></td>
									<td><a
										href="${pageContext.request.contextPath}/admin/prodotto?id=${prodotto.id}"
										class="btn-action">Gestisci</a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty prodotti}">
								<tr>
									<td colspan="7"
										style="text-align: center; color: #666; padding: 30px;">Nessun
										prodotto corrispondente ai criteri di ricerca.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<c:if test="${totalPages > 1}">
					<c:set var="queryFiltri"
						value="&ordinamento=${param.ordinamento}&prodotto_id=${param.prodotto_id}&categoria_id=${param.categoria_id}&stato_prodotto=${param.stato_prodotto}&minimo=${param.minimo}&massimo=${param.massimo}" />

					<div class="pagination-container">
						<c:if test="${currentPage > 1}">
							<a href="?pagina=${currentPage - 1}${queryFiltri}">&laquo;
								Prec</a>
						</c:if>

						<c:forEach begin="1" end="${totalPages}" var="i">
							<c:choose>
								<c:when test="${i == currentPage}">
									<span class="active">${i}</span>
								</c:when>
								<c:otherwise>
									<a href="?pagina=${i}${queryFiltri}">${i}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:if test="${currentPage < totalPages}">
							<a href="?pagina=${currentPage + 1}${queryFiltri}">Succ
								&raquo;</a>
						</c:if>
					</div>
				</c:if>

			</section>
		</div>
	</main>

	<script
		src="${pageContext.request.contextPath}/scripts/admin-dashboard.js"></script>
	<script
		src="${pageContext.request.contextPath}/scripts/admin-prodotti.js"></script>

</body>
</html>