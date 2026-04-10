<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><c:choose>
		<c:when test="${admin.id == 0}">Nuovo Membro Team</c:when>
		<c:otherwise>Admin #${admin.id} | CarryCrew</c:otherwise>
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
	href="${pageContext.request.contextPath}/styles/admin-dettaglio-team.css">
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

			<div class="team-detail-container">

				<form id="adminTeamForm"
					action="${pageContext.request.contextPath}/admin/membroteam"
					method="POST">

					<input type="hidden" name="id" value="${admin.id}"> <input
						type="hidden" name="azione" id="azioneInput" value=""> <input
						type="hidden" id="isNewAdmin"
						value="${admin.id == 0 ? 'true' : 'false'}">

					<div class="team-header">
						<h1>
							<c:choose>
								<c:when test="${admin.id == 0}">Aggiungi Nuovo Admin</c:when>
								<c:otherwise>Profilo Amministratore: #${admin.id}</c:otherwise>
							</c:choose>
						</h1>
						<c:if test="${isAdminCorrente}">
							<div class="info-text"
								style="color: #2ecc71; margin-top: 10px; font-weight: bold;">
								<i class="fas fa-user-circle"></i> Stai visualizzando il tuo
								account personale
							</div>
						</c:if>
					</div>

					<div class="form-group">
						<label for="email">Indirizzo Email (Username)</label> <input
							type="email" id="email" name="email"
							value="<c:out value='${admin.email}' />" class="form-input"
							placeholder="admin@dominio.it">
						<div id="err-email" class="alert-error"
							style="display: none; margin-top: 10px">
							<i class="fas fa-exclamation-triangle"></i> <span
								id="err-email-text"></span>
						</div>
					</div>

					<div class="form-group">
						<label for="password">Password di Accesso</label> <input
							type="password" id="password" name="password" class="form-input"
							placeholder="${admin.id == 0 ? 'Inserisci una password sicura...' : 'Lascia vuoto per non modificare la password'}">
						<div id="err-password" class="alert-error"
							style="display: none; margin-top: 10px">
							<i class="fas fa-exclamation-triangle"></i> <span
								id="err-password-text"></span>
						</div>
						<c:if test="${admin.id != 0}">
							<span class="info-text">Attenzione: inserendo testo in
								questo campo, sovrascriverai la password esistente.</span>
						</c:if>
					</div>

					<div class="action-buttons">

						<c:if test="${admin.id != 0}">
							<c:choose>
								<c:when test="${eliminazionePermessa}">
									<button type="button" id="btnElimina" class="btn-danger">
										<i class="fas fa-trash"></i> Revoca Accesso (Elimina)
									</button>
								</c:when>
								<c:otherwise>
									<span class="disabled-info"
										title="L'eliminazione non è permessa per il tuo stesso account o per l'unico admin rimasto">
										<i class="fas fa-ban"></i> Eliminazione non permessa
									</span>
								</c:otherwise>
							</c:choose>
						</c:if>

						<a href="${pageContext.request.contextPath}/admin/team"
							class="btn-secondary">Annulla</a>

						<button type="submit" class="btn-primary">
							<c:choose>
								<c:when test="${admin.id == 0}">Crea Account Admin</c:when>
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
		src="${pageContext.request.contextPath}/scripts/admin-dettaglio-team.js"></script>

</body>
</html>