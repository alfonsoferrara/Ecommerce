<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Checkout - Il Mio Ecommerce</title>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
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

.total-line {
	display: flex;
	justify-content: space-between;
	margin: 10px 0;
}

.total-final {
	font-size: 24px;
	font-weight: bold;
	color: #ff6b6b;
	margin-top: 15px;
	padding-top: 15px;
	border-top: 2px solid #ddd;
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

		<form action="${pageContext.request.contextPath}/checkout"
			method="POST" id="checkoutForm">

			<!-- SEZIONE INDIRIZZO -->
			<div class="section">
				<h2 class="section-title">Indirizzo di spedizione</h2>

				<c:choose>
					<c:when test="${not empty indirizzi}">
						<div class="radio-group">
							<input type="radio" name="opzioneIndirizzo" value="esistente"
								id="usaEsistente" checked> <label for="usaEsistente">Usa
								un indirizzo esistente</label> <select name="indirizzoId"
								style="margin-top: 10px;">
								<option value="">-- Seleziona un indirizzo --</option>
								<c:forEach var="ind" items="${indirizzi}">
									<option value="${ind.id}">${ind.via}${ind.civico},
										${ind.citta} (${ind.provincia}) - ${ind.cap}</option>
								</c:forEach>
							</select>
						</div>

						<div class="radio-group">
							<input type="radio" name="opzioneIndirizzo" value="nuovo"
								id="usaNuovo"> <label for="usaNuovo">Usa un
								nuovo indirizzo</label>
						</div>

						<div id="nuovoIndirizzo" style="display: none;"
							class="indirizzo-nuovo">
							<div class="row">
								<div>
									<label>Via *</label> <input type="text" name="via">
								</div>
								<div>
									<label>Civico *</label> <input type="text" name="civico">
								</div>
							</div>
							<div class="row">
								<div>
									<label>Città *</label> <input type="text" name="citta">
								</div>
								<div>
									<label>Provincia *</label> <input type="text" name="provincia"
										maxlength="2" placeholder="es. RM">
								</div>
								<div>
									<label>CAP *</label> <input type="text" name="cap"
										maxlength="5" placeholder="es. 00100">
								</div>
							</div>
						</div>
					</c:when>

					<c:otherwise>
						<!-- NUOVO INDIRIZZO -->
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
					name="metodoPagamento" id="metodoPagamento" required>
					<option value="">-- Seleziona --</option>
					<c:forEach var="metodo" items="${metodiDiPagamento}">
						<option value="${metodo}">${metodo}</option>
					</c:forEach>
				</select>
			</div>

			<!-- SEZIONE NOTE -->
			<div class="section">
				<h2 class="section-title">Note (opzionali)</h2>
				<textarea name="notaCliente" rows="3"
					placeholder="Eventuali richieste o note..."></textarea>
			</div>

			<!-- RIEPILOGO ORDINE -->
			<div class="section">
				<h2 class="section-title">Riepilogo ordine</h2>
				<div class="total-box">
					<div class="total-line">
						<span>Subtotale prodotti:</span> <span id="subtotale">€
							${String.format("%.2f", subtotale)}</span>
					</div>
					<div class="total-line">
						<span>Spedizione:</span> <span id="spedizione">€
							${String.format("%.2f", speseSpedizione)}</span>
					</div>
					<div id="contraccoltoRow" class="total-line" style="display: none;">
						<span>Costo contrassegno:</span> <span>€ 5.00</span>
					</div>
					<hr>
					<div class="total-line total-final">
						<span><strong>Totale da pagare:</strong></span> <span
							id="totaleFinale"><strong>€
								${String.format("%.2f", totaleFinale)}</strong></span>
					</div>
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

	<script>
		// Valori iniziali dalla servlet
		var subtotale = $
		{
			subtotale
		};
		var speseSpedizione = $
		{
			speseSpedizione
		};
		var totaleBase = $
		{
			totaleFinale
		};
		var costoContrassegno = 5.00;

		// Elementi DOM
		var metodoSelect = document.getElementById('metodoPagamento');
		var spedizioneSpan = document.getElementById('spedizione');
		var totaleSpan = document.getElementById('totaleFinale');
		var contrassegnoRow = document.getElementById('contraccoltoRow');

		// Funzione per aggiornare i totali
		function aggiornaTotali() {
			var metodo = metodoSelect.value;
			var costoExtra = 0;

			if (metodo === 'Contrassegno') {
				costoExtra = costoContrassegno;
				contrassegnoRow.style.display = 'flex';
			} else {
				contrassegnoRow.style.display = 'none';
			}

			var nuovoTotale = totaleBase + costoExtra;
			totaleSpan.innerHTML = '<strong>€ ' + nuovoTotale.toFixed(2)
					+ '</strong>';
		}

		// Aggiungi event listener quando cambia il metodo di pagamento
		if (metodoSelect) {
			metodoSelect.addEventListener('change', aggiornaTotali);
		}

		// Gestione toggle indirizzo
		<c:if test="${not empty indirizzi}">
		var usaEsistente = document.getElementById('usaEsistente');
		var usaNuovo = document.getElementById('usaNuovo');
		var nuovoDiv = document.getElementById('nuovoIndirizzo');
		var selectIndirizzo = document
				.querySelector('select[name="indirizzoId"]');

		if (usaEsistente && usaNuovo && nuovoDiv) {
			function toggleIndirizzo() {
				if (usaEsistente.checked) {
					nuovoDiv.style.display = 'none';
					if (selectIndirizzo)
						selectIndirizzo.required = true;
					var nuoviCampi = nuovoDiv.querySelectorAll('input');
					nuoviCampi.forEach(function(input) {
						input.required = false;
					});
				} else {
					nuovoDiv.style.display = 'block';
					if (selectIndirizzo)
						selectIndirizzo.required = false;
					var nuoviCampi = nuovoDiv.querySelectorAll('input');
					nuoviCampi.forEach(function(input) {
						if (input.name)
							input.required = true;
					});
				}
			}

			usaEsistente.onclick = toggleIndirizzo;
			usaNuovo.onclick = toggleIndirizzo;
			toggleIndirizzo();
		}
		</c:if>
	</script>
</body>
</html>