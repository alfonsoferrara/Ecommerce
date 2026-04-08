<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dettaglio Ordine #${ordine.id} | CarryCrew Admin</title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-dashboard.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-dettagli-ordine.css">
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

			<div style="margin-bottom: 30px;">
				<h1 style="font-size: 2rem; font-weight: 900; color: #fff;">ID
					ORDINE: #${ordine.id}</h1>
				<p style="color: #888; font-weight: 600;">
					Effettuato il:
					<fmt:formatDate value="${ordine.data}" pattern="dd/MM/yyyy HH:mm" />
				</p>
			</div>

			<div class="info-grid">
				<div class="info-box">
					<h3>
						<i class="fas fa-user"></i> Dati Cliente
					</h3>
					<div class="info-row">
						<strong>ID Cliente:</strong> <span>${cliente.id}</span>
					</div>
					<div class="info-row">
						<strong>Nome:</strong> <span>${cliente.nome}
							${cliente.cognome}</span>
					</div>
					<div class="info-row">
						<strong>Email:</strong> <span>${cliente.email}</span>
					</div>
					<div class="info-row">
						<strong>Telefono:</strong> <span>${cliente.telefono}</span>
					</div>
				</div>

				<div class="info-box">
					<h3>
						<i class="fas fa-map-marker-alt"></i> Indirizzo di Spedizione
					</h3>
					<div class="info-row">
						<strong>Via:</strong> <span>${indirizzo.via},
							${indirizzo.civico}</span>
					</div>
					<div class="info-row">
						<strong>Città:</strong> <span>${indirizzo.citta}
							(${indirizzo.provincia})</span>
					</div>
					<div class="info-row">
						<strong>CAP:</strong> <span>${indirizzo.cap}</span>
					</div>
				</div>

				<c:if test="${not empty ordine.notaCliente}">
					<div class="info-box customer-note-box">
						<h3>
							<i class="fas fa-comment-dots"></i> Note del Cliente
						</h3>
						<p>&quot;${ordine.notaCliente}&quot;</p>
					</div>
				</c:if>
			</div>

			<section class="dashboard-section" style="margin-bottom: 0;">
				<h2>Dettagli Prodotti Acquistati</h2>
				<div class="admin-table-wrapper">
					<table class="admin-table">
						<thead>
							<tr>
								<th>ID Prodotto</th>
								<th>Nome Prodotto</th>
								<th>Quantità</th>
								<th>Prezzo Unitario</th>
								<th style="text-align: right;">Subtotale</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${dettagli}" var="entry">
								<tr>
									<td>#${entry.value.id}</td>
									<td><strong>${entry.value.nome}</strong></td>
									<td>${entry.key.quantita}</td>
									<td><fmt:formatNumber value="${entry.key.prezzoUnitario}"
											type="currency" currencySymbol="€" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											value="${entry.key.prezzoUnitario * entry.key.quantita}"
											type="currency" currencySymbol="€" /></td>
								</tr>
							</c:forEach>

							<tr class="table-totals">
								<td colspan="4" style="text-align: right; color: #888;">Totale
									Prodotti:</td>
								<td style="text-align: right;"><fmt:formatNumber
										value="${subtotale}" type="currency" currencySymbol="€" /></td>
							</tr>
							<tr class="table-totals">
								<td colspan="4" style="text-align: right; color: #888;">Spese
									di Spedizione:</td>
								<td style="text-align: right;"><fmt:formatNumber
										value="${speseSpedizione}" type="currency" currencySymbol="€" /></td>
							</tr>
							<c:if test="${costoContrassegno > 0}">
								<tr class="table-totals">
									<td colspan="4" style="text-align: right; color: #888;">Supplemento
										Contrassegno:</td>
									<td style="text-align: right;"><fmt:formatNumber
											value="${costoContrassegno}" type="currency"
											currencySymbol="€" /></td>
								</tr>
							</c:if>
							<tr class="table-totals grand-total">
								<td colspan="4" style="text-align: right;">TOTALE ORDINE:</td>
								<td style="text-align: right; color: #fff;"><fmt:formatNumber
										value="${ordine.totale}" type="currency" currencySymbol="€" /></td>
							</tr>
						</tbody>
					</table>
				</div>
			</section>

			<div class="summary-status-section">

				<div class="payment-summary">
					<h4>Metodo di Pagamento</h4>
					<div class="payment-method-badge">
						<i class="fas fa-credit-card" style="margin-right: 8px;"></i>
						${ordine.metodoPagamento}
					</div>
				</div>


				<!-- LOGICA PER STATO DI DEFAULT -->
				<div class="status-update-form">
					<h4>Gestisci Stato Ordine</h4>
					<form id="statusUpdateForm"
						action="${pageContext.request.contextPath}/admin/dettagliOrdine"
						method="POST">
						<input type="hidden" name="id" value="${ordine.id}">

						<div class="status-controls">
							<select name="statoCambiato" id="statoCambiato"
								class="status-select">
								<option value="In elaborazione"
									${ordine.stato == 'IN ELABORAZIONE' ? 'selected' : ''}>In
									elaborazione</option>
								<option value="Spedito"
									${ordine.stato == 'SPEDITO' ? 'selected' : ''}>Spedito</option>
								<option value="Consegnato"
									${ordine.stato == 'CONSEGNATO' ? 'selected' : ''}>Consegnato</option>
								<option value="Annullato"
									${ordine.stato == 'ANNULLATO' ? 'selected' : ''}>Annullato</option>
							</select>
							<button type="submit" class="btn-update">Aggiorna Stato</button>
						</div>
					</form>
				</div>

			</div>

		</div>
	</main>

	<script
		src="${pageContext.request.contextPath}/scripts/admin-dashboard.js"></script>
	<script
		src="${pageContext.request.contextPath}/scripts/admin-dettagli-ordine.js"></script>

</body>
</html>