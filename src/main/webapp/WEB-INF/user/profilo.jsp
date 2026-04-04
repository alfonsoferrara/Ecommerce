<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Il mio Profilo - CarryCrew</title>
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/profiloUtente.css">
</head>
<body>

	<jsp:include
		page="/WEB-INF/views/fragments/header.jsp" />

	<main class="profile-wrapper">

		<div class="profile-header">
			<h1>
				<i class="fas fa-user-circle"></i> Il mio Profilo
			</h1>
			<p class="welcome-message">
				<span id="dynamic-greeting">Ciao</span>, <strong>${utenteLoggato.nome}
					${utenteLoggato.cognome}</strong>!<br> Benvenuto nella tua area
				personale.
			</p>
		</div>

		<div class="dashboard-grid">

			<div class="dashboard-card">
				<div class="card-icon">
					<i class="fas fa-box-open"></i>
				</div>
				<div class="card-title">I miei Ordini</div>
				<div class="card-description">Visualizza lo storico dei tuoi
					ordini, controlla lo stato delle spedizioni e scarica le ricevute.</div>
				<a href="${pageContext.request.contextPath}/user/ordini"
					class="btn-card"> Vedi ordini <i class="fas fa-arrow-right"
					style="margin-left: 5px;"></i>
				</a>
			</div>

			<div class="dashboard-card">
				<div class="card-icon">
					<i class="fas fa-map-marker-alt"></i>
				</div>
				<div class="card-title">Gestisci Indirizzi</div>
				<div class="card-description">Aggiungi o elimina i
					tuoi indirizzi di spedizione per i futuri acquisti.</div>
				<a href="${pageContext.request.contextPath}/user/indirizzi"
					class="btn-card"> I miei indirizzi <i
					class="fas fa-arrow-right" style="margin-left: 5px;"></i>
				</a>
			</div>

			<div class="dashboard-card">
				<div class="card-icon">
					<i class="fas fa-user-edit"></i>
				</div>
				<div class="card-title">Modifica Dati</div>
				<div class="card-description">Aggiorna le tue informazioni
					personali, l'indirizzo email e la password di accesso.</div>
				<a href="${pageContext.request.contextPath}/user/informazioni"
					class="btn-card"> Modifica profilo <i
					class="fas fa-arrow-right" style="margin-left: 5px;"></i>
				</a>
			</div>

		</div>

		<div class="logout-section">
			<a href="${pageContext.request.contextPath}/logout"
				class="logout-link" id="logout-link"> <i
				class="fas fa-sign-out-alt"></i> Esci dal tuo account
			</a>
		</div>

	</main>

	<jsp:include
		page="/WEB-INF/views/fragments/footer.jsp" />

	<script
		src="${pageContext.request.contextPath}/scripts/profiloUtente.js"></script>

</body>
</html>