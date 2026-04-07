<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestione Ordini | CarryCrew Admin</title>

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
	href="${pageContext.request.contextPath}/styles/admin-ordini.css">
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
				<h3>Filtra Ordini</h3>
				<form action="${pageContext.request.contextPath}/admin/ordini"
					method="GET" class="filter-form">

					<div class="filter-group">
						<label for="ordinamentoSelect">Cerca per:</label> <select
							name="ordinamento" id="ordinamentoSelect">
							<option value="recenti"
								${param.ordinamento == 'recenti' ? 'selected' : ''}>Ultimi
								Ordini (Recenti)</option>
							<option value="ordineId"
								${param.ordinamento == 'ordineId' ? 'selected' : ''}>ID
								Ordine</option>
							<option value="clienteID"
								${param.ordinamento == 'clienteID' ? 'selected' : ''}>ID
								Cliente</option>
							<option value="stato"
								${param.ordinamento == 'stato' ? 'selected' : ''}>Stato
								Ordine</option>
							<option value="metodoPagamento"
								${param.ordinamento == 'metodoPagamento' ? 'selected' : ''}>Metodo
								Pagamento</option>
							<option value="rangeData"
								${param.ordinamento == 'rangeData' ? 'selected' : ''}>Range
								di Date</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-ordine">
						<label>ID Ordine</label> <input type="number" name="id_ordine"
							value="${param.id_ordine}" placeholder="Es. 123">
					</div>

					<div class="filter-group filter-hidden" id="group-cliente">
						<label>ID Cliente</label> <input type="number" name="id_cliente"
							value="${param.id_cliente}" placeholder="Es. 45">
					</div>

					<div class="filter-group filter-hidden" id="group-stato">
						<label>Stato</label> <select name="stato_ordine">
							<option value="">Seleziona...</option>
							<option value="In elaborazione"
								${param.stato_ordine == 'In elaborazione' ? 'selected' : ''}>In
								elaborazione</option>
							<option value="Spedito"
								${param.stato_ordine == 'Spedito' ? 'selected' : ''}>Spedito</option>
							<option value="Consegnato"
								${param.stato_ordine == 'Consegnato' ? 'selected' : ''}>Consegnato</option>
							<option value="Annullato"
								${param.stato_ordine == 'Annullato' ? 'selected' : ''}>Annullato</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-pagamento">
						<label>Pagamento</label> <select name="pagamento">
							<option value="">Seleziona...</option>
							<option value="Contrassegno"
								${param.pagamento == 'Contrassegno' ? 'selected' : ''}>Contrassegno</option>
							<option value="Carta di credito/debito"
								${param.pagamento == 'Carta di credito/debito' ? 'selected' : ''}>Carta
								di credito/debito</option>
							<option value="Klarna"
								${param.pagamento == 'Klarna' ? 'selected' : ''}>Klarna</option>
							<option value="PayPal"
								${param.pagamento == 'PayPal' ? 'selected' : ''}>PayPal</option>
						</select>
					</div>

					<div class="filter-group filter-hidden" id="group-date-inizio">
						<label>Data Inizio</label> <input type="date" name="dataInizio"
							value="${param.dataInizio}">
					</div>
					<div class="filter-group filter-hidden" id="group-date-fine">
						<label>Data Fine</label> <input type="date" name="dataFine"
							value="${param.dataFine}">
					</div>

					<div class="filter-group">
						<button type="submit" class="btn-filter">Applica Filtro</button>
					</div>
				</form>
			</div>

			<section class="dashboard-section">
				<h2>Elenco Ordini</h2>
				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID Ordine</th>
								<th>ID Cliente</th>
								<th>Data</th>
								<th>Metodo Pagamento</th>
								<th>Stato</th>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ordini}" var="ordine">
								<tr>
									<td><strong>#${ordine.id}</strong></td>
									<td>${ordine.clienteId}</td>
									<td><fmt:formatDate value="${ordine.data}"
											pattern="dd/MM/yyyy HH:mm" /></td>
									<td>${ordine.metodoPagamento}</td>
									<td><c:choose>
											<c:when
												test="${ordine.stato == 'SPEDITO' || ordine.stato == 'CONSEGNATO'}">
												<span class="status-badge status-spedito">${ordine.stato}</span>
											</c:when>
											<c:when test="${ordine.stato == 'ANNULLATO'}">
												<span class="status-badge status-annullato">${ordine.stato}</span>
											</c:when>
											<c:otherwise>
												<span class="status-badge status-elaborazione">${ordine.stato}</span>
											</c:otherwise>
										</c:choose></td>
									<td><a
										href="${pageContext.request.contextPath}/admin/ordine?id=${ordine.id}"
										class="btn-action">Vedi Dettagli</a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty ordini}">
								<tr>
									<td colspan="6"
										style="text-align: center; color: #666; padding: 30px;">Nessun
										ordine corrispondente ai filtri di ricerca.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<c:if test="${totalPages > 1}">
					<c:set var="queryFiltri"
						value="&ordinamento=${param.ordinamento}&id_ordine=${param.id_ordine}&id_cliente=${param.id_cliente}&stato_ordine=${param.stato_ordine}&pagamento=${param.pagamento}&dataInizio=${param.dataInizio}&dataFine=${param.dataFine}" />

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
		src="${pageContext.request.contextPath}/scripts/admin-ordini.js"></script>

</body>
</html>