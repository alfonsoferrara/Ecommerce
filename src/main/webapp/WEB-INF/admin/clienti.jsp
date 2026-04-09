<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestione Clienti | CarryCrew Admin</title>

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
	href="${pageContext.request.contextPath}/styles/admin-clienti.css">
</head>
<body>

	<jsp:include page="/WEB-INF/admin/fragments/admin-sidebar.jsp" />

	<main class="admin-main-content">

		<jsp:include page="/WEB-INF/admin/fragments/admin-topbar.jsp" />

		<div class="dashboard-content">

			<c:if test="${not empty erroreClienti}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${erroreClienti}
				</div>
			</c:if>

			<div class="filter-panel">
				<h3>Ricerca e Ordinamento</h3>
				<form action="${pageContext.request.contextPath}/admin/clienti"
					method="GET" class="filter-form">

					<div class="filter-group">
						<label for="ordinamentoSelect">Ordina/Cerca per:</label> <select
							name="ordinamento" id="ordinamentoSelect">
							<option value="recenti"
								${param.ordinamento == 'recenti' ? 'selected' : ''}>Ultimi
								iscritti (Recenti)</option>
							<option value="alfabeticoNome"
								${param.ordinamento == 'alfabeticoNome' ? 'selected' : ''}>Ordine
								Alfabetico (Nome A-Z)</option>
							<option value="alfabeticoCognome"
								${param.ordinamento == 'alfabeticoCognome' ? 'selected' : ''}>Ordine
								Alfabetico (Cognome A-Z)</option>
							<option value="findById"
								${param.ordinamento == 'findById' ? 'selected' : ''}>Ricerca
								per ID Cliente</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-cliente-id">
						<label>ID Cliente</label> <input type="number" name="id_cliente"
							value="${param.id_cliente}" placeholder="Es. 10">
					</div>

					<div class="filter-group">
						<button type="submit" class="btn-filter">Applica Filtro</button>
					</div>
					<div class="filter-group">
						<button type="button" class="btn-filter"
							onclick="window.location.href='${pageContext.request.contextPath}/admin/clienti'">Reset</button>
					</div>
				</form>
			</div>

			<section class="dashboard-section">

				<div class="section-title-wrapper">
					<h2>Elenco Clienti Registrati</h2>
					<a href="${pageContext.request.contextPath}/admin/cliente?id=new"
						class="btn-add"> <i class="fas fa-user-plus"></i> Nuovo
						Cliente
					</a>
				</div>

				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nome</th>
								<th>Cognome</th>
								<th>Indirizzo Email</th>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${clienti}" var="cliente">
								<tr>
									<td><strong>#${cliente.id}</strong></td>
									<td>${cliente.nome}</td>
									<td><strong>${cliente.cognome}</strong></td>
									<td style="color: #aaa;">${cliente.email}</td>
									<td><a
										href="${pageContext.request.contextPath}/admin/cliente?id=${cliente.id}"
										class="btn-action">Vedi Dettagli</a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty clienti}">
								<tr>
									<td colspan="5"
										style="text-align: center; color: #666; padding: 30px;">Nessun
										cliente trovato con questi criteri di ricerca.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<c:if test="${pagineTotali > 1}">
					<c:set var="queryFiltri"
						value="&ordinamento=${param.ordinamento}&id_cliente=${param.id_cliente}" />

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
		src="${pageContext.request.contextPath}/scripts/admin-clienti.js"></script>

</body>
</html>