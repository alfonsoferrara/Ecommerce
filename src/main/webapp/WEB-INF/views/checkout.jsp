<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Checkout - CarryCrew</title>

<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/checkout.css">
</head>
<body>

	<jsp:include page="fragments/header.jsp" />

	<main class="checkout-wrapper">
		<div class="checkout-header">
			<h1>
				<i class="fas fa-lock"></i> Checkout Sicuro
			</h1>
		</div>

		<c:if test="${not empty sessionScope.errore}">
			<div class="alert-error">
				<i class="fas fa-exclamation-triangle"></i> ${sessionScope.errore}
			</div>
			<c:remove var="errore" scope="session" />
		</c:if>

		<form action="${pageContext.request.contextPath}/checkout"
			method="POST" id="checkoutForm">

			<div class="section">
				<h2 class="section-title">
					<i class="fas fa-map-marker-alt"></i> Indirizzo di Spedizione
				</h2>

				<c:choose>
					<c:when test="${not empty indirizzi}">
						<div class="radio-group">
							<input type="radio" name="opzioneIndirizzo" value="esistente"
								id="usaEsistente" checked> <label for="usaEsistente">Usa
								un indirizzo salvato</label> <select name="indirizzoId"
								style="margin-top: 15px;">
								<option value="">-- Seleziona un indirizzo --</option>
								<c:forEach var="ind" items="${indirizzi}">
									<option value="${ind.id}">${ind.via}${ind.civico},
										${ind.citta} (${ind.provincia}) - ${ind.cap}</option>
								</c:forEach>
							</select>
						</div>

						<div class="radio-group">
							<input type="radio" name="opzioneIndirizzo" value="nuovo"
								id="usaNuovo"> <label for="usaNuovo">Inserisci
								un nuovo indirizzo</label>
						</div>

						<div id="nuovoIndirizzo" style="display: none;"
							class="indirizzo-nuovo">
							<div class="form-row">
								<div>
									<label>Via *</label> <input type="text" name="via"
										placeholder="Es: Via Roma">
								</div>
								<div>
									<label>Civico *</label> <input type="text" name="civico"
										placeholder="Es: 12/B">
								</div>
							</div>
							<div class="form-row">
								<div>
									<label>Città *</label> <input type="text" name="citta">
								</div>
								<div>
									<label>Provincia *</label> <input type="text" name="provincia"
										maxlength="2" placeholder="Es: RM">
								</div>
								<div>
									<label>CAP *</label> <input type="text" name="cap"
										maxlength="5" placeholder="Es: 00100">
								</div>
							</div>
						</div>
					</c:when>

					<c:otherwise>
						<input type="hidden" name="opzioneIndirizzo" value="nuovo">
						<div class="form-row">
							<div>
								<label>Via *</label> <input type="text" name="via" required
									placeholder="Es: Via Roma">
							</div>
							<div>
								<label>Civico *</label> <input type="text" name="civico"
									required placeholder="Es: 12/B">
							</div>
						</div>
						<div class="form-row">
							<div>
								<label>Città *</label> <input type="text" name="citta" required>
							</div>
							<div>
								<label>Provincia *</label> <input type="text" name="provincia"
									maxlength="2" placeholder="Es: RM" required>
							</div>
							<div>
								<label>CAP *</label> <input type="text" name="cap" maxlength="5"
									placeholder="Es: 00100" required>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="section">
				<h2 class="section-title">
					<i class="fas fa-credit-card"></i> Metodo di Pagamento
				</h2>
				<label>Seleziona come vuoi pagare *</label> <select
					name="metodoPagamento" id="metodoPagamento" required>
					<option value="">-- Seleziona --</option>
					<c:forEach var="metodo" items="${metodiDiPagamento}">
						<option value="${metodo}">${metodo}</option>
					</c:forEach>
				</select>
			</div>

			<div class="section">
				<h2 class="section-title">
					<i class="fas fa-comment-alt"></i> Note (Opzionali)
				</h2>
				<textarea name="notaCliente" rows="3"
					placeholder="Eventuali istruzioni per la consegna..."></textarea>
			</div>

			<div class="section"
				style="border: none; padding: 0; background: transparent;">
				<div class="total-box">
					<div class="total-line">
						<span>Subtotale prodotti:</span> <span id="subtotale">€
							${String.format("%.2f", subtotale)}</span>
					</div>
					<div class="total-line">
						<span>Spedizione:</span> <span id="spedizione">€
							${String.format("%.2f", speseSpedizione)}</span>
					</div>
					<div id="contraccoltoRow" class="total-line"
						style="display: none; color: #ffcccc;">
						<span>Costo contrassegno:</span> <span>€ 5.00</span>
					</div>
					<hr>
					<div class="total-line total-final">
						<span>Totale da pagare:</span> <span id="totaleFinaleSpan">€
							${String.format("%.2f", totaleFinale)}</span>
					</div>
				</div>
			</div>

			<div class="actions">
				<a href="${pageContext.request.contextPath}/carrello"
					class="btn btn-outline"> <i class="fas fa-arrow-left"></i>
					Torna al Carrello
				</a>
				<button type="submit" class="btn">
					Conferma Ordine <i class="fas fa-check" style="margin-left: 8px;"></i>
				</button>
			</div>
		</form>
	</main>

	<jsp:include page="fragments/footer.jsp" />

	<script>
		window.totaleBase = parseFloat("${totaleFinale}".replace(',', '.'));
	</script>

	<script src="${pageContext.request.contextPath}/scripts/checkout.js"></script>

</body>
</html>