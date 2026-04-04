<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>I miei indirizzi - CarryCrew</title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/indirizzi.css">
</head>
<body>

	<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

	<main class="addresses-wrapper">
		<div class="addresses-header">
			<h1>
				<i class="fas fa-map-marker-alt"></i> Gestisci Indirizzi
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

		<c:choose>
			<c:when test="${empty indirizzi}">
				<div class="empty-addresses">
					<i class="far fa-address-card"
						style="font-size: 40px; margin-bottom: 15px; color: #ccc;"></i>
					<p style="margin: 0;">Non hai ancora salvato nessun indirizzo.</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class="addresses-grid">
					<c:forEach var="indirizzo" items="${indirizzi}">
						<div class="address-card">
							<div>
								<div class="address-info">
									<strong>${indirizzo.via} ${indirizzo.civico}</strong><br>
									${indirizzo.citta} (${indirizzo.provincia}) - ${indirizzo.cap}
								</div>
								<form action="${pageContext.request.contextPath}/user/indirizzi"
									method="POST" style="margin: 0;">
									<input type="hidden" name="action" value="delete"> <input
										type="hidden" name="indirizzoId" value="${indirizzo.id}">
									<button type="submit" class="btn-danger"
										onclick="return confirm('Sei sicuro di voler eliminare questo indirizzo?')">
										<i class="fas fa-trash-alt"></i> Elimina
									</button>
								</form>
							</div>
							<div class="disabled-msg">
								<i class="fas fa-info-circle"></i> Gli indirizzi salvati non
								sono modificabili per preservare la validità delle ricevute
								degli ordini passati.
							</div>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>

		<div class="form-add-section">
			<h3>
				<i class="fas fa-plus-circle"></i> Aggiungi nuovo indirizzo
			</h3>

			<form id="addAddressForm"
				action="${pageContext.request.contextPath}/user/indirizzo"
				method="POST" novalidate>

				<div class="form-row">
					<div class="form-group">
						<label for="via">Via *</label> <input type="text" id="via"
							name="via" class="form-control" placeholder="es. Via Roma"
							required>
						<div class="error-message"></div>
					</div>
					<div class="form-group" style="flex: 0.4;">
						<label for="civico">Civico *</label> <input type="text"
							id="civico" name="civico" class="form-control"
							placeholder="es. 12/B" required>
						<div class="error-message"></div>
					</div>
				</div>

				<div class="form-row">
					<div class="form-group">
						<label for="citta">Città *</label> <input type="text" id="citta"
							name="citta" class="form-control" placeholder="es. Milano"
							required>
						<div class="error-message"></div>
					</div>
					<div class="form-group">
						<label for="provincia">Provincia *</label> <input type="text"
							id="provincia" name="provincia" class="form-control"
							maxlength="2" placeholder="es. MI" required>
						<div class="error-message"></div>
					</div>
					<div class="form-group">
						<label for="cap">CAP *</label> <input type="text" id="cap"
							name="cap" class="form-control" maxlength="5"
							placeholder="es. 20100" required>
						<div class="error-message"></div>
					</div>
				</div>

				<button type="submit" class="btn" style="margin-top: 10px;">
					Salva Indirizzo <i class="fas fa-save" style="margin-left: 5px;"></i>
				</button>
			</form>
		</div>

		<div>
			<a href="${pageContext.request.contextPath}/user/profilo"
				class="back-link"> <i class="fas fa-arrow-left"></i> Torna al
				profilo
			</a>
		</div>
	</main>

	<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />

	<script src="${pageContext.request.contextPath}/scripts/indirizzi.js"></script>

</body>
</html>