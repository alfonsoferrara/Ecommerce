<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ordine Confermato - Il Mio Ecommerce</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background-color: #f5f5f5;
}

.container {
	max-width: 900px;
	margin: 0 auto;
	background: white;
	padding: 30px;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
	color: #28a745;
	border-bottom: 2px solid #28a745;
	padding-bottom: 10px;
}

h2 {
	color: #333;
	margin-top: 30px;
}

.order-info {
	background-color: #f8f9fa;
	padding: 15px;
	border-radius: 5px;
	margin: 20px 0;
}

.order-number {
	font-size: 24px;
	font-weight: bold;
	color: #ff6b6b;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin: 20px 0;
}

th, td {
	padding: 12px;
	text-align: left;
	border-bottom: 1px solid #ddd;
}

th {
	background-color: #f8f9fa;
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

.btn {
	display: inline-block;
	padding: 12px 25px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	margin-top: 20px;
	margin-right: 10px;
}

.btn-secondary {
	background-color: #6c757d;
}

.address-box {
	background-color: #f8f9fa;
	padding: 15px;
	border-radius: 5px;
	margin: 10px 0;
}

.thank-you {
	font-size: 18px;
	margin: 20px 0;
}

.price {
	color: #ff6b6b;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="container">
		<h1>✅ Ordine Confermato!</h1>

		<div class="thank-you">
			<p>Grazie per aver acquistato da noi! Il tuo ordine è stato
				ricevuto e sarà elaborato a breve.</p>
		</div>

		<div class="order-info">
			<p>
				<strong>Numero Ordine:</strong> <span class="order-number">#${ordine.id}</span>
			</p>
			<p>
				<strong>Data Ordine:</strong> ${ordine.data}
			</p>
			<p>
				<strong>Stato:</strong> ${ordine.stato}
			</p>
			<p>
				<strong>Metodo di Pagamento:</strong> ${ordine.metodoPagamento}
			</p>
			<c:if test="${not empty ordine.notaCliente}">
				<p>
					<strong>Note:</strong> ${ordine.notaCliente}
				</p>
			</c:if>
		</div>

		<h2>📦 Prodotti Ordinati</h2>
		<table>
			<thead>
				<tr>
					<th>Prodotto</th>
					<th>Prezzo Unitario</th>
					<th>Quantità</th>
					<th>Totale</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${dettagliMap}">
					<c:set var="dettaglio" value="${entry.key}" />
					<c:set var="prodotto" value="${entry.value}" />
					<c:set var="totaleProdotto"
						value="${dettaglio.prezzoUnitario * dettaglio.quantita}" />
					<tr>
						<td><strong>${prodotto.nome}</strong></td>
						<td class="price">€ ${String.format("%.2f", dettaglio.prezzoUnitario)}</td>
						<td>${dettaglio.quantita}</td>
						<td class="price">€ ${String.format("%.2f", totaleProdotto)}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<h2>📍 Indirizzo di Spedizione</h2>
		<div class="address-box">
			<p>
				<strong>${indirizzo.via} ${indirizzo.civico}</strong>
			</p>
			<p>
				<strong>${indirizzo.citta} (${indirizzo.provincia}) -
					${indirizzo.cap}</strong>
			</p>
		</div>

		<h2>💰 Riepilogo Pagamento</h2>
		<div class="order-info">
			<div class="total-line">
				<span>Subtotale prodotti:</span> <span>€
					${String.format("%.2f", subtotale)}</span>
			</div>
			<div class="total-line">
				<span>Spedizione:</span> <span>€ ${String.format("%.2f", speseSpedizione)}</span>
			</div>
			<c:if test="${costoContrassegno > 0}">
				<div class="total-line">
					<span>Costo contrassegno:</span> <span>€
						${String.format("%.2f", costoContrassegno)}</span>
				</div>
			</c:if>
			<div class="total-line total-final">
				<span><strong>TOTALE PAGATO:</strong></span> <span><strong>€
						${String.format("%.2f", ordine.totale)}</strong></span>
			</div>
		</div>

		<div style="text-align: center;">
			<a href="${pageContext.request.contextPath}/home" class="btn">Continua
				lo shopping</a> <a href="${pageContext.request.contextPath}/user/ordini"
				class="btn btn-secondary">I miei ordini</a>
		</div>
	</div>
</body>
</html>