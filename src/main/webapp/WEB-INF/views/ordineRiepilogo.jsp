<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Ordine Confermato - CarryCrew</title>
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/riepilogo.css">
</head>
<body>

	<jsp:include page="fragments/header.jsp" />

	<main class="riepilogo-wrapper">

		<div class="success-header">
			<h1>
				<i class="fas fa-check-circle"></i> Ordine Confermato!
			</h1>
			<p class="thank-you-text">Grazie per il tuo acquisto. Abbiamo
				ricevuto il tuo ordine e lo stiamo elaborando.</p>
		</div>

		<h2 class="section-title">
			<i class="fas fa-info-circle"></i> Informazioni Ordine
		</h2>
		<div class="info-box">
			<p>
				<strong>Numero Ordine:</strong> <span class="order-number">#${ordine.id}</span>
			</p>
			<p>
				<strong>Data Ordine:</strong> ${ordine.data}
			</p>
			<p>
				<strong>Stato Attuale:</strong> ${ordine.stato}
			</p>
			<p>
				<strong>Metodo di Pagamento:</strong> ${ordine.metodoPagamento}
			</p>
			<c:if test="${not empty ordine.notaCliente}">
				<p>
					<strong>Note fornite:</strong> ${ordine.notaCliente}
				</p>
			</c:if>
		</div>

		<h2 class="section-title">
			<i class="fas fa-map-marker-alt"></i> Indirizzo di Spedizione
		</h2>
		<div class="info-box">
			<p style="margin: 0; font-size: 16px;">
				${indirizzo.via} ${indirizzo.civico}<br> ${indirizzo.cap} -
				${indirizzo.citta} (${indirizzo.provincia})
			</p>
		</div>

		<h2 class="section-title">
			<i class="fas fa-box-open"></i> Prodotti Ordinati
		</h2>
		<table>
			<thead>
				<tr>
					<th>Prodotto</th>
					<th>Prezzo Unitario</th>
					<th>Quantità</th>
					<th style="text-align: right;">Totale</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${dettagli}">
					<c:set var="dettaglio" value="${entry.key}" />
					<c:set var="prodotto" value="${entry.value}" />
					<c:set var="totaleProdotto"
						value="${dettaglio.prezzoUnitario * dettaglio.quantita}" />
					<tr>
						<td><strong>${prodotto.nome}</strong></td>
						<td class="price">€ ${String.format("%.2f", dettaglio.prezzoUnitario)}</td>
						<td>${dettaglio.quantita}</td>
						<td class="price" style="text-align: right;">€
							${String.format("%.2f", totaleProdotto)}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="totals-box">
			<div class="total-line">
				<span>Subtotale:</span> <span>€ ${String.format("%.2f", subtotale)}</span>
			</div>
			<div class="total-line">
				<span>Spedizione:</span> <span>€ ${String.format("%.2f", speseSpedizione)}</span>
			</div>
			<c:if test="${costoContrassegno > 0}">
				<div class="total-line">
					<span>Contrassegno:</span> <span>€ ${String.format("%.2f", costoContrassegno)}</span>
				</div>
			</c:if>
			<hr>
			<div class="total-line total-final">
				<span>Totale Pagato:</span> <span>€ ${String.format("%.2f", ordine.totale)}</span>
			</div>
		</div>

		<div class="actions">
			<a href="${pageContext.request.contextPath}/home" class="btn"> <i
				class="fas fa-shopping-bag"></i> Continua lo shopping
			</a> <a href="${pageContext.request.contextPath}/user/ordini"
				class="btn btn-outline"> <i class="fas fa-list"></i> I miei
				ordini
			</a>
			<button id="btn-stampa-ricevuta" class="btn btn-outline">
				<i class="fas fa-print"></i> Stampa
			</button>
		</div>

	</main>

	<jsp:include page="fragments/footer.jsp" />

	<script src="${pageContext.request.contextPath}/scripts/riepilogo.js"></script>

</body>
</html>