<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestione Attributi | CarryCrew Admin</title>

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
	href="${pageContext.request.contextPath}/styles/admin-attributi.css">
</head>
<body>

	<jsp:include page="/WEB-INF/admin/fragments/admin-sidebar.jsp" />

	<main class="admin-main-content">

		<jsp:include page="/WEB-INF/admin/fragments/admin-topbar.jsp" />

		<div class="dashboard-content">

			<c:if test="${not empty erroreAttributi}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${erroreAttributi}
				</div>
			</c:if>
			<c:if test="${not empty operazioneRiuscita}">
				<div class="alert-success">
					<i class="fas fa-check-circle"></i> ${operazioneRiuscita}
				</div>
			</c:if>
			<c:if test="${not empty errore}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${errore}
				</div>
			</c:if>

			<div class="filter-panel">
				<h3>Ricerca e Ordinamento</h3>
				<form action="${pageContext.request.contextPath}/admin/attributi"
					method="GET" class="filter-form">

					<div class="filter-group">
						<label for="ordinamentoSelect">Ordina/Cerca per:</label> <select
							name="ordinamento" id="ordinamentoSelect">
							<option value="recenti"
								${param.ordinamento == 'recenti' ? 'selected' : ''}>Ordine
								di Creazione (Più recenti prima)</option>
							<option value="alfabetico"
								${param.ordinamento == 'alfabetico' ? 'selected' : ''}>Ordine
								Alfabetico (A-Z)</option>
							<option value="findById"
								${param.ordinamento == 'findById' ? 'selected' : ''}>Ricerca
								per ID Attributo</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-attributo-id">
						<label>ID Attributo</label> <input type="number"
							name="id_attributo" value="${param.id_attributo}"
							placeholder="Es. 5">
					</div>

					<div class="filter-group">
						<button type="submit" class="btn-filter">Applica Filtro</button>
					</div>

					<div class="filter-group">
						<button type="button" class="btn-filter"
							onclick="window.location.href='${pageContext.request.contextPath}/admin/attributi'">Reset</button>
					</div>

				</form>
			</div>

			<section class="dashboard-section">

				<div class="section-title-wrapper">
					<h2>Elenco Attributi (Specifiche Prodotti)</h2>
					<a href="${pageContext.request.contextPath}/admin/attributo?id=new"
						class="btn-add"> <i class="fas fa-plus"></i> Nuovo Attributo
					</a>
				</div>

				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th style="width: 15%;">ID</th>
								<th style="width: 60%;">Nome Attributo</th>
								<th style="width: 25%;">Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${attributAdmin}" var="attr">
								<tr>
									<td><strong>#${attr.id}</strong></td>
									<td><strong>${attr.nome}</strong></td>
									<td><a
										href="${pageContext.request.contextPath}/admin/attributo?id=${attr.id}"
										class="btn-action">Gestisci / Modifica</a></td>
								</tr>
							</c:forEach>

							<c:if test="${empty attributAdmin}">
								<tr>
									<td colspan="3"
										style="text-align: center; color: #666; padding: 30px;">
										Nessun attributo trovato con questi criteri.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<c:if test="${pagineTotali > 1}">
					<c:set var="queryFiltri"
						value="&ordinamento=${param.ordinamento}&id_attributo=${param.id_attributo}" />

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
		src="${pageContext.request.contextPath}/scripts/admin-attributi.js"></script>

</body>
</html>