<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestione Team | CarryCrew Admin</title>

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
	href="${pageContext.request.contextPath}/styles/admin-team.css">
</head>
<body>

	<jsp:include page="/WEB-INF/admin/fragments/admin-sidebar.jsp" />

	<main class="admin-main-content">

		<jsp:include page="/WEB-INF/admin/fragments/admin-topbar.jsp" />

		<div class="dashboard-content">

			<c:if test="${not empty erroreTeam}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${erroreTeam}
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
				<h3>Ricerca e Ordinamento Team</h3>
				<form action="${pageContext.request.contextPath}/admin/team"
					method="GET" class="filter-form">

					<div class="filter-group">
						<label for="ordinamentoSelect">Ordina/Cerca per:</label> <select
							name="ordinamento" id="ordinamentoSelect">
							<option value="recenti"
								${param.ordinamento == 'recenti' ? 'selected' : ''}>Ordine
								di Creazione (Più recenti)</option>
							<option value="alfabetico"
								${param.ordinamento == 'alfabetico' ? 'selected' : ''}>Ordine
								Alfabetico (Email A-Z)</option>
							<option value="findById"
								${param.ordinamento == 'findById' ? 'selected' : ''}>Ricerca
								per ID Membro</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-admin-id">
						<label>ID Admin</label> <input type="number" name="id_admin"
							value="${param.id_admin}" placeholder="Es. 1">
					</div>

					<div class="filter-group">
						<button type="submit" class="btn-filter">Applica Filtro</button>
					</div>
					<div class="filter-group">
						<button type="button" class="btn-filter"
							onclick="window.location.href='${pageContext.request.contextPath}/admin/team'">Reset</button>
					</div>
				</form>
			</div>

			<section class="dashboard-section">

				<div class="section-title-wrapper">
					<h2>Elenco Membri del Team</h2>
					<a
						href="${pageContext.request.contextPath}/admin/membroteam?id=new"
						class="btn-add"> <i class="fas fa-user-shield"></i> Nuovo
						Membro
					</a>
				</div>

				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID</th>
								<th>Indirizzo Email</th>
								<th>Data di Creazione</th>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${adminList}" var="admin">
								<tr>
									<td><strong>#${admin.id}</strong></td>
									<td><strong>${admin.email}</strong></td>
									<td style="color: #aaa;"><fmt:formatDate
											value="${admin.dataCreazione}" pattern="dd/MM/yyyy HH:mm" />
									</td>
									<td><a
										href="${pageContext.request.contextPath}/admin/membroteam?id=${admin.id}"
										class="btn-action">Gestisci</a></td>
								</tr>
							</c:forEach>

							<c:if test="${empty adminList}">
								<tr>
									<td colspan="4"
										style="text-align: center; color: #666; padding: 30px;">
										Nessun membro del team trovato.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<c:if test="${pagineTotali > 1}">
					<c:set var="queryFiltri"
						value="&ordinamento=${param.ordinamento}&id_admin=${param.id_admin}" />

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
	<script src="${pageContext.request.contextPath}/scripts/admin-team.js"></script>

</body>
</html>