<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Checkout - Il Mio Ecommerce</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background-color: #f5f5f5;
}

.container {
	max-width: 800px;
	margin: 0 auto;
	background: white;
	padding: 20px;
	border-radius: 5px;
}

h1, h2, h3 {
	color: #333;
}

h1 {
	border-bottom: 2px solid #ff6b6b;
	padding-bottom: 10px;
}

.section {
	margin-bottom: 25px;
	padding: 15px;
	border: 1px solid #ddd;
	border-radius: 5px;
}

.section-title {
	margin-top: 0;
	margin-bottom: 15px;
	color: #ff6b6b;
}

label {
	display: block;
	margin-top: 10px;
	margin-bottom: 5px;
	font-weight: bold;
}

input, select, textarea {
	width: 100%;
	padding: 8px;
	border: 1px solid #ddd;
	border-radius: 4px;
	box-sizing: border-box;
}

input[type="radio"] {
	width: auto;
	margin-right: 10px;
}

.radio-group {
	margin: 10px 0;
}

.radio-group label {
	display: inline;
	font-weight: normal;
}

.indirizzo-nuovo {
	margin-top: 15px;
	padding: 15px;
	background-color: #f9f9f9;
	border-radius: 5px;
}

.row {
	display: flex;
	gap: 15px;
}

.row>div {
	flex: 1;
}

.btn {
	display: inline-block;
	padding: 12px 25px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	border: none;
	cursor: pointer;
	font-size: 16px;
}

.btn:hover {
	background-color: #ff5252;
}

.btn-secondary {
	background-color: #6c757d;
}

.btn-secondary:hover {
	background-color: #5a6268;
}

.actions {
	margin-top: 30px;
	display: flex;
	justify-content: space-between;
}

.error {
	background-color: #f8d7da;
	color: #721c24;
	padding: 10px;
	border-radius: 5px;
	margin-bottom: 20px;
}

.success {
	background-color: #d4edda;
	color: #155724;
	padding: 10px;
	border-radius: 5px;
	margin-bottom: 20px;
}

.total-box {
	background-color: #f8f9fa;
	padding: 15px;
	border-radius: 5px;
	margin-top: 20px;
}

hr {
	margin: 20px 0;
}
</style>
</head>
<body>
	<div class="container">
		<h1>Checkout</h1>

		<c:if test="${not empty sessionScope.errore}">
			<div class="error">${sessionScope.errore}</div>
			<c:remove var="errore" scope="session" />
		</c:if>

		<c:if test="${not empty sessionScope.messaggio}">
			<div class="success">${sessionScope.messaggio}</div>
			<c:remove var="messaggio" scope="session" />
		</c:if>

		<form action="${pageContext.request.contextPath}/checkout"
			method="POST">

			<!-- SEZIONE INDIRIZZO -->
			<div class="section">
				<h2 class="section-title">Indirizzo di spedizione</h2>

				<c:choose>
					<c:when test="${not empty indirizzi}">
						<!-- Opzione: indirizzo esistente -->
						<div class="radio-group">
							<input type="radio" name="opzioneIndirizzo" value="esistente"
								id="usaEsistente" checked> <label for="usaEsistente">Usa
								un indirizzo esistente</label> <select name="indirizzoId"
								style="margin-top: 10px;">
								<option value="">-- Seleziona un indirizzo --</option>
								<c:forEach var="ind" items="${indirizzi}">
									<option value="${ind.id}">${ind.via} ${ind.civico},
										${ind.citta} (${ind.provincia}) - ${ind.cap}</option>
								</c:forEach>
							</select>
						</div>

						<!-- Opzione: nuovo indirizzo -->
						<div class="radio-group">
							<input type="radio" name="opzioneIndirizzo" value="nuovo"
								id="usaNuovo"> <label for="usaNuovo">Usa un
								nuovo indirizzo</label>
						</div>

						<div id="nuovoIndirizzo" style="display: none;"
							class="indirizzo-nuovo">
							<div class="row">
								<div>
									<label>Via *</label> <input type="text" name="via"
										value="${param.via}">
								</div>
								<div>
									<label>Civico *</label> <input type="text" name="civico"
										value="${param.civico}">
								</div>
							</div>
							<div class="row">
								<div>
									<label>Città *</label> <input type="text" name="citta"
										value="${param.citta}">
								</div>
								<div>
									<label>Provincia *</label> <input type="text" name="provincia"
										maxlength="2" placeholder="es. RM" value="${param.provincia}">
								</div>
								<div>
									<label>CAP *</label> <input type="text" name="cap"
										maxlength="5" placeholder="es. 00100" value="${param.cap}">
								</div>
							</div>
							<small style="color: #666;">* Campi obbligatori</small>
						</div>
					</c:when>

					<c:otherwise>
						<!-- Nessun indirizzo esistente: solo form nuovo -->
						<input type="hidden" name="opzioneIndirizzo" value="nuovo">
						<div class="row">
							<div>
								<label>Via *</label> <input type="text" name="via" required>
							</div>
							<div>
								<label>Civico *</label> <input type="text" name="civico"
									required>
							</div>
						</div>
						<div class="row">
							<div>
								<label>Città *</label> <input type="text" name="citta" required>
							</div>
							<div>
								<label>Provincia *</label> <input type="text" name="provincia"
									maxlength="2" placeholder="es. RM" required>
							</div>
							<div>
								<label>CAP *</label> <input type="text" name="cap" maxlength="5"
									placeholder="es. 00100" required>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</div>

			<!-- SEZIONE METODO DI PAGAMENTO -->
			<div class="section">
				<h2 class="section-title">Metodo di pagamento</h2>
				<label>Seleziona come vuoi pagare *</label> <select
					name="metodoPagamento" required>
					<option value="">-- Seleziona --</option>
					<c:forEach var="metodo" items="${metodiDiPagamento}">
						<option value="${metodo}"
							${param.metodoPagamento == metodo ? 'selected' : ''}>
							${metodo}</option>
					</c:forEach>
				</select>
			</div>

			<!-- SEZIONE NOTE -->
			<div class="section">
				<h2 class="section-title">Note (opzionali)</h2>
				<label>Note per il venditore</label>
				<textarea name="notaCliente" rows="3"
					placeholder="Eventuali richieste o note...">${param.notaCliente}</textarea>
			</div>

			<!-- RIEPILOGO ORDINE -->
			<div class="section">
				<h2 class="section-title">Riepilogo ordine</h2>
				<div class="total-box">
					<p>
						<strong>Subtotale:</strong> € ${subtotale}
					</p>
					<p>
						<strong>Spedizione:</strong> € ${speseSpedizione}
					</p>
					<hr>
					<p style="font-size: 24px; margin: 0;">
						<strong>Totale da pagare:</strong> € ${totaleFinale}
					</p>
				</div>
			</div>

			<!-- BOTTONI -->
			<div class="actions">
				<a href="${pageContext.request.contextPath}/carrello"
					class="btn btn-secondary">← Torna al carrello</a>
				<button type="submit" class="btn">Conferma ordine</button>
			</div>
		</form>
	</div>

	<!-- JavaScript minimo solo per toggle indirizzo (opzionale, ma utile) -->
	<c:if test="${not empty indirizzi}">
		<script>
			var usaEsistente = document.getElementById('usaEsistente');
			var usaNuovo = document.getElementById('usaNuovo');
			var nuovoDiv = document.getElementById('nuovoIndirizzo');
			var selectIndirizzo = document
					.querySelector('select[name="indirizzoId"]');

			if (usaEsistente && usaNuovo && nuovoDiv) {
				function toggleIndirizzo() {
					if (usaEsistente.checked) {
						nuovoDiv.style.display = 'none';
						selectIndirizzo.required = true;
						// Rimuovi required dai campi nuovo indirizzo
						var nuoviCampi = nuovoDiv.querySelectorAll('input');
						nuoviCampi.forEach(function(input) {
							input.required = false;
						});
					} else {
						nuovoDiv.style.display = 'block';
						selectIndirizzo.required = false;
						// Aggiungi required ai campi nuovo indirizzo
						var nuoviCampi = nuovoDiv.querySelectorAll('input');
						nuoviCampi.forEach(function(input) {
							if (input.name !== undefined) {
								input.required = true;
							}
						});
					}
				}

				usaEsistente.onclick = toggleIndirizzo;
				usaNuovo.onclick = toggleIndirizzo;
				toggleIndirizzo(); // stato iniziale
			}
		</script>
	</c:if>
</body>
</html>