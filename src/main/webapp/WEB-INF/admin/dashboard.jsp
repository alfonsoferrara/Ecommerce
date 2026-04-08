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
</head>
<body>

	<jsp:include page="/WEB-INF/admin/fragments/admin-sidebar.jsp" />

	<main class="admin-main-content">

		<jsp:include page="/WEB-INF/admin/fragments/admin-topbar.jsp" />

		<div class="dashboard-content">

			<section class="dashboard-section">
				<h2>Ultimi 10 Ordini</h2>
				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID Ordine</th>
								<th>Data</th>
								<th>Stato</th>
								<th>Metodo di Pagamento</th>
								<th>Totale</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="sommaTotale" value="0" />

							<c:forEach items="${ordini}" var="ordine">
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
									<td>${ordine.metodoPagamento}</td>
									<td><fmt:formatNumber value="${ordine.totale}"
											type="currency" currencySymbol="€" maxFractionDigits="2" /></td>
								</tr>
								<!-- Somma solo se lo stato NON è ANNULLATO -->
								<c:if test="${ordine.stato != 'ANNULLATO'}">
									<c:set var="sommaTotale" value="${sommaTotale + ordine.totale}" />
								</c:if>
							</c:forEach>

							<c:if test="${not empty ordini}">
								<tr class="total-row">
									<td colspan="4" style="text-align: right;">TOTALE
										INCASSATO (Ultimi ordini):</td>
									<td><fmt:formatNumber value="${sommaTotale}"
											type="currency" currencySymbol="€" maxFractionDigits="2" /></td>
								</tr>
							</c:if>

							<c:if test="${empty ordini}">
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
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${prodotti}" var="item">
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
								</tr>
							</c:forEach>

							<c:if test="${empty prodotti}">
								<tr>
									<td colspan="4" style="text-align: center; color: #2ecc71;">Ottimo!
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