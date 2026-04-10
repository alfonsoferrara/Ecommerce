<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><c:choose>
		<c:when test="${cliente.id == 0}">Nuovo Cliente</c:when>
		<c:otherwise>Cliente #${cliente.id} | CarryCrew Admin</c:otherwise>
	</c:choose></title>

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
	href="${pageContext.request.contextPath}/styles/admin-dettaglio-cliente.css">
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
			<c:if test="${not empty messaggio}">
				<div class="alert-success">
					<i class="fas fa-check-circle"></i> ${messaggio}
				</div>
			</c:if>

			<c:if test="${not empty errore}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${errore}
				</div>
			</c:if>

			<div class="customer-detail-container">

				<form id="clienteForm"
					action="${pageContext.request.contextPath}/admin/cliente"
					method="POST">

					<input type="hidden" name="id" value="${cliente.id}"> <input
						type="hidden" id="isNewCustomer"
						value="${cliente.id == 0 ? 'true' : 'false'}">

					<div class="customer-header">
						<h1>
							<c:choose>
								<c:when test="${cliente.id == 0}">Crea Nuovo Cliente</c:when>
								<c:otherwise>Anagrafica Cliente: #${cliente.id}</c:otherwise>
							</c:choose>
						</h1>
					</div>

					<div class="form-grid">
						<div class="form-group">
							<label for="nome">Nome</label> <input type="text" id="nome"
								name="nome" value="<c:out value='${cliente.nome}' />"
								class="form-input"> <span class="error-text"
								id="err-nome"></span>
						</div>

						<div class="form-group">
							<label for="cognome">Cognome</label> <input type="text"
								id="cognome" name="cognome"
								value="<c:out value='${cliente.cognome}' />" class="form-input">
							<span class="error-text" id="err-cognome"></span>
						</div>

						<div class="form-group">
							<label for="telefono">Telefono</label> <input type="text"
								id="telefono" name="telefono"
								value="<c:out value='${cliente.telefono}' />" class="form-input">
							<span class="error-text" id="err-telefono"></span>
						</div>

						<div class="form-group">
							<label for="email">Indirizzo Email</label> <input type="email"
								id="email" name="email"
								value="<c:out value='${cliente.email}' />" class="form-input">
							<span class="error-text" id="err-email"></span>
						</div>

						<div class="form-group">
							<label for="password">Password</label> <input type="password"
								id="password" name="password" class="form-input"
								placeholder="${cliente.id == 0 ? 'Inserisci una password (min. 6 caratteri)' : 'Lascia vuoto per mantenere quella attuale'}">
							<span class="error-text" id="err-password"></span>
						</div>
					</div>

					<c:if test="${cliente.id != 0}">

						<div class="history-section">
							<h3>
								<i class="fas fa-box"></i> Storico Ordini
							</h3>
							<c:choose>
								<c:when test="${not empty ordini}">
									<div class="admin-table-wrapper">
										<table class="admin-table">
											<thead>
												<tr>
													<th>ID Ordine</th>
													<th>Data Effettuazione</th>
													<th>Stato Ordine</th>
													<th>Totale</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${ordini}" var="ordine">
													<tr>
														<td><a
															href="${pageContext.request.contextPath}/admin/dettagliOrdine?id=${ordine.id}"
															style="color: #fff; text-decoration: underline;">#${ordine.id}</a>
														</td>
														<td><fmt:formatDate value="${ordine.data}"
																pattern="dd/MM/yyyy - HH:mm" /></td>
														<td><strong>${ordine.stato}</strong></td>
														<td><fmt:formatNumber value="${ordine.totale}"
																type="currency" currencySymbol="€" /></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</c:when>
								<c:otherwise>
									<p style="color: #666; font-style: italic;">Questo cliente
										non ha ancora effettuato ordini.</p>
								</c:otherwise>
							</c:choose>
						</div>

						<div class="history-section">
							<h3>
								<i class="fas fa-map-marker-alt"></i> Indirizzi di Spedizione
								Salvati
							</h3>
							<c:choose>
								<c:when test="${not empty indirizzi}">
									<div class="admin-table-wrapper">
										<table class="admin-table">
											<thead>
												<tr>
													<th>Via</th>
													<th>Civico</th>
													<th>Città</th>
													<th>Provincia</th>
													<th>CAP</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${indirizzi}" var="ind">
													<tr>
														<td>${ind.via}</td>
														<td>${ind.civico}</td>
														<td><strong>${ind.citta}</strong></td>
														<td>${ind.provincia}</td>
														<td>${ind.cap}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</c:when>
								<c:otherwise>
									<p style="color: #666; font-style: italic;">Nessun
										indirizzo salvato per questo cliente.</p>
								</c:otherwise>
							</c:choose>
						</div>

					</c:if>

					<div class="action-buttons">

						<c:if test="${cliente.id != 0}">
							<button type="submit" name="azione" value="elimina"
								class="btn-secondary btn-elimina"
								onclick="return confirm('Sei sicuro di voler eliminare questo cliente?');">
								<i class="fas fa-trash"></i> Elimina Cliente
							</button>
						</c:if>

						<a href="${pageContext.request.contextPath}/admin/clienti"
							class="btn-secondary">Annulla</a>

						<button type="submit" class="btn-primary">
							<c:choose>
								<c:when test="${cliente.id == 0}">Salva Nuovo Cliente</c:when>
								<c:otherwise>Salva Modifiche</c:otherwise>
							</c:choose>
						</button>
					</div>

				</form>
			</div>
		</div>
	</main>

	<script
		src="${pageContext.request.contextPath}/scripts/admin-dashboard.js"></script>
	<script
		src="${pageContext.request.contextPath}/scripts/admin-dettaglio-cliente.js"></script>

</body>
</html>