<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestione Categorie | CarryCrew Admin</title>

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
	href="${pageContext.request.contextPath}/styles/admin-categorie.css">
</head>
<body>

	<jsp:include page="/WEB-INF/admin/fragments/admin-sidebar.jsp" />

	<main class="admin-main-content">

		<jsp:include page="/WEB-INF/admin/fragments/admin-topbar.jsp" />

		<div class="dashboard-content">

			<c:if test="${not empty errorePagCategoria}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${errorePagCategoria}
				</div>
			</c:if>
			<c:if test="${not empty messaggio}">
				<div class="alert-success">
					<i class="fas fa-exclamation-triangle"></i> ${messaggio}
				</div>
			</c:if>
			<c:if test="${not empty operazioneRiuscita}">
				<div class="alert-success">
					<i class="fas fa-exclamation-triangle"></i> ${operazioneRiuscita}
				</div>
			</c:if>
			
			<div class="filter-panel">
				<h3>Opzioni di Visualizzazione</h3>
				<form action="${pageContext.request.contextPath}/admin/categorie"
					method="GET" class="filter-form">

					<div class="filter-group">
						<label for="ordinamentoSelect">Ordina Categorie per:</label> <select
							name="ordinamento" id="ordinamentoSelect">
							<option value="recenti"
								${param.ordinamento == 'recenti' ? 'selected' : ''}>Ordine
								di Creazione (Più recenti prima)</option>
							<option value="alfabetico"
								${param.ordinamento == 'alfabetico' ? 'selected' : ''}>Ordine
								Alfabetico (A-Z)</option>
						</select>
					</div>

					<div class="filter-group">
						<button type="submit" class="btn-filter">Applica Filtro</button>
					</div>
					<div class="filter-group">
						<button type="button" class="btn-filter"
							onclick="window.location.href='${pageContext.request.contextPath}/admin/categorie'">Resetta</button>
					</div>
				</form>
			</div>

			<section class="dashboard-section">

				<div class="section-title-wrapper">
					<h2>Elenco Categorie</h2>
					<a href="${pageContext.request.contextPath}/admin/categoria?id=new"
						class="btn-add"> <i class="fas fa-plus"></i> Nuova Categoria
					</a>
				</div>

				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nome Categoria</th>
								<th>Descrizione</th>
								<th style="text-align: center;">Prodotti Contenuti</th>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${categorie}" var="cat">
								<tr>
									<td><strong>#${cat.id}</strong></td>
									<td><strong>${cat.nome}</strong></td>

									<td class="desc-cell">
										<div class="text-truncate-2" title="${cat.descrizione}">
											${cat.descrizione}</div>
									</td>

									<td style="text-align: center;"><c:set var="countByName"
											value="${numeroProdotti[cat.nome]}" /> <c:choose>
											<c:when test="${not empty countByName}">
												<span class="badge-count">${countByName}</span>
											</c:when>
											<c:otherwise>
												<span class="badge-count"
													style="background-color: transparent; border-color: #333; color: #888;">0</span>
											</c:otherwise>
										</c:choose></td>

									<td><a
										href="${pageContext.request.contextPath}/admin/categoria?id=${cat.id}"
										class="btn-action">Gestisci</a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty categorie}">
								<tr>
									<td colspan="5"
										style="text-align: center; color: #666; padding: 30px;">Nessuna
										categoria trovata nel sistema.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<c:if test="${pagineTotali > 1}">
					<c:set var="queryFiltri" value="&ordinamento=${param.ordinamento}" />

					<div class="pagination-container">
						<c:if test="${paginaCorrente > 1}">
							<a href="?pagina=${paginaCorrente - 1}${queryFiltri}">&laquo;
								Prec</a>
						</c:if>

						<c:forEach begin="1" end="${pagineTotali}" var="i">
							<c:choose>
								<c:when test="${i == paginaCorrente}">
									<span class="active">${i}</span>
								</c:when>
								<c:otherwise>
									<a href="?pagina=${i}${queryFiltri}">${i}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:if test="${paginaCorrente < pagineTotali}">
							<a href="?pagina=${paginaCorrente + 1}${queryFiltri}">Succ
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
		src="${pageContext.request.contextPath}/scripts/admin-categorie.js"></script>

</body>
</html>