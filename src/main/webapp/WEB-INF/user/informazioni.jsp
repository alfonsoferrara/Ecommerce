<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Le mie informazioni - CarryCrew</title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/informazioniUtente.css">
</head>
<body>

	<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

	<main class="info-wrapper">
		<div class="info-header">
			<h1>
				<i class="fas fa-user-edit"></i> Modifica Profilo
			</h1>
		</div>

		<c:if test="${not empty sessionScope.messaggio}">
			<div class="alert-message alert-success">
				<i class="fas fa-check-circle"></i> ${sessionScope.messaggio}
			</div>
			<c:remove var="messaggio" scope="session" />
		</c:if>

		<c:if test="${not empty sessionScope.errore}">
			<div class="alert-message alert-error">
				<i class="fas fa-exclamation-triangle"></i> ${sessionScope.errore}
			</div>
			<c:remove var="errore" scope="session" />
		</c:if>

		<form id="infoForm"
			action="${pageContext.request.contextPath}/user/informazioni"
			method="POST" novalidate>

			<div class="form-row">
				<div class="form-group">
					<label for="nome">Nome *</label> <input type="text" id="nome"
						name="nome" value="${cliente.nome}" class="form-control" required>
					<div class="error-message"></div>
				</div>

				<div class="form-group">
					<label for="cognome">Cognome *</label> <input type="text"
						id="cognome" name="cognome" value="${cliente.cognome}"
						class="form-control" required>
					<div class="error-message"></div>
				</div>
			</div>

			<div class="form-group">
				<label for="email">Email *</label> <input type="email" id="email"
					name="email" value="${cliente.email}" class="form-control" required>
				<div class="error-message"></div>
			</div>

			<div class="form-group">
				<label for="telefono">Telefono</label> <input type="tel"
					id="telefono" name="telefono" value="${cliente.telefono}"
					class="form-control" placeholder="Opzionale">
				<div class="error-message"></div>
			</div>

			<hr>

			<div class="form-group">
				<label for="password"><i class="fas fa-lock"></i> Nuova
					Password (lasciare vuoto per non cambiarla)</label> <input type="password"
					id="password" name="password" class="form-control"
					placeholder="Inserisci la nuova password"> <i
					class="fas fa-eye password-toggle" id="togglePassword"></i>
				<div class="error-message"></div>
			</div>

			<div class="buttons-group">
				<button type="submit" class="btn">
					Salva Modifiche <i class="fas fa-save" style="margin-left: 5px;"></i>
				</button>
				<a href="${pageContext.request.contextPath}/user/profilo"
					class="btn btn-secondary">Annulla</a>
			</div>
		</form>

		<div>
			<a href="${pageContext.request.contextPath}/user/profilo"
				class="back-link"> <i class="fas fa-arrow-left"></i> Torna al
				profilo
			</a>
		</div>
	</main>

	<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />

	<script
		src="${pageContext.request.contextPath}/scripts/informazioniUtente.js"></script>

</body>
</html>