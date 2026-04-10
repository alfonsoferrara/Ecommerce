<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard | CarryCrew Admin</title>

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
				<div class="alert-error"
					style="background-color: rgba(231, 76, 60, 0.1); border-left: 4px solid #e74c3c; color: #e74c3c; padding: 15px; margin-bottom: 20px;">
					<i class="fas fa-exclamation-triangle"></i> ${errore}
				</div>
			</c:if>

			<div class="kpi-date-filter">
				<form action="${pageContext.request.contextPath}/admin/dashboard"
					method="GET" class="kpi-date-form">
					<label>Filtra Incassi:</label> <input type="date" name="dataInizio"
						value="${param.dataInizio}" title="Data Inizio"> <input
						type="date" name="dataFine" value="${param.dataFine}"
						title="Data Fine">
					<button type="submit" class="btn-kpi-filter">Aggiorna</button>
					<a href="${pageContext.request.contextPath}/admin/dashboard"
						class="btn-kpi-reset">Reset</a>
				</form>
			</div>

			<div class="kpi-grid">

				<div class="kpi-box">
					<div class="kpi-icon revenue">
						<i class="fas fa-euro-sign"></i>
					</div>
					<div class="kpi-details">
						<h4>
							Totale Incassato
							<c:if test="${empty param.dataFine and empty param.dataInizio}">
								<span style="font-size: 0.9rem;">(Mese corrente)</span>
							</c:if>
						</h4>
						<span class="kpi-value"> <fmt:formatNumber
								value="${totaleVendite}" type="currency" currencySymbol="€"
								maxFractionDigits="2" />
						</span>
					</div>
				</div>

				<div class="kpi-box">
					<div class="kpi-icon pending">
						<i class="fas fa-box-open"></i>
					</div>
					<div class="kpi-details">
						<h4>Ordini da Evadere</h4>
						<span class="kpi-value"><a
							href="${pageContext.request.contextPath}/admin/ordini?ordinamento=stato&stato_ordine=In+elaborazione"
							style="color: white;">${ordiniDaEvadere}</a></span>
					</div>
				</div>

				<div class="kpi-box">
					<div class="kpi-icon stock">
						<i class="fas fa-exclamation-circle"></i>
					</div>
					<div class="kpi-details">
						<h4>Stock Critico (<= 5)</h4>
						<span class="kpi-value"><a
							href="${pageContext.request.contextPath}/admin/prodotti?ordinamento=stock&massimo=5"
							style="color: white;">${prodottiInEsaurimento}</a> <span
							style="font-size: 0.9rem; font-weight: normal; color: #888;">prod.</span></span>
					</div>
				</div>

			</div>
			<section class="dashboard-section">
				<h2>Ultimi 5 Ordini</h2>
				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID Ordine</th>
								<th>Data</th>
								<th>Stato</th>
								<th>Totale</th>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="sommaTotale" value="0" />

							<c:forEach items="${ordiniTab}" var="ordine">
								<tr>
									<td>#${ordine.id}</td>
									<td><fmt:formatDate value="${ordine.data}"
											pattern="dd/MM/yyyy HH:mm" /></td>
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
									<td><fmt:formatNumber value="${ordine.totale}"
											type="currency" currencySymbol="€" maxFractionDigits="2" /></td>
									<td><a
										href="${pageContext.request.contextPath}/admin/dettagliOrdine?id=${ordine.id}"
										class="btn-action">Vedi Dettagli</a></td>
								</tr>
								<c:if test="${ordine.stato != 'ANNULLATO'}">
									<c:set var="sommaTotale" value="${sommaTotale + ordine.totale}" />
								</c:if>
							</c:forEach>

							<c:if test="${empty ordiniTab}">
								<tr>
									<td colspan="5" style="text-align: center; color: #666;">Nessun
										ordine recente trovato.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</section>

			<section class="dashboard-section">
				<h2>Prodotti Terminati (Stock 0)</h2>
				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID Prodotto</th>
								<th>Nome Prodotto</th>
								<th>Prezzo</th>
								<th>Data Esaurimento (Ultimo Acq.)</th>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${prodottiTerminati}" var="item">
								<tr>
									<td>#${item.prodotto.id}</td>
									<td><strong>${item.prodotto.nome}</strong></td>
									<td><fmt:formatNumber value="${item.prodotto.prezzo}"
											type="currency" currencySymbol="€" maxFractionDigits="2" /></td>
									<td><c:choose>
											<c:when test="${empty item.ultimoAcquisto}">
												<span style="color: #666;">N/D</span>
											</c:when>
											<c:otherwise>
                                                ${item.ultimoAcquisto}
                                            </c:otherwise>
										</c:choose></td>
									<td><a
										href="${pageContext.request.contextPath}/admin/prodotto?id=${item.prodotto.id}"
										class="btn-action">Vedi Dettagli</a></td>
								</tr>
							</c:forEach>

							<c:if test="${empty prodottiTerminati}">
								<tr>
									<td colspan="5" style="text-align: center; color: #2ecc71;">Ottimo!
										Nessun prodotto è esaurito al momento.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</section>

		</div>
	</main>

	<script
		src="${pageContext.request.contextPath}/scripts/admin-dashboard.js"></script>

</body>
</html>