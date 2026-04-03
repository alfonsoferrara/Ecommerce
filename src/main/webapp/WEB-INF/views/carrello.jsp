<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Carrello - Il Mio Ecommerce</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background-color: #f5f5f5;
}

.container {
	max-width: 1000px;
	margin: 0 auto;
	background: white;
	padding: 20px;
	border-radius: 5px;
}

h1 {
	color: #333;
	border-bottom: 2px solid #ff6b6b;
	padding-bottom: 10px;
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

.price {
	color: #ff6b6b;
	font-weight: bold;
}

.total {
	text-align: right;
	font-size: 18px;
	font-weight: bold;
	margin-top: 20px;
	padding-top: 10px;
	border-top: 2px solid #ddd;
}

.btn {
	display: inline-block;
	padding: 10px 20px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	border: none;
	cursor: pointer;
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

.btn-danger {
	background-color: #dc3545;
}

.btn-danger:hover {
	background-color: #c82333;
	cursor: pointer;
}

.quantity {
	width: 60px;
	text-align: center;
}

.empty-cart {
	text-align: center;
	padding: 50px;
}

.actions {
	margin-top: 20px;
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
</style>
</head>
<body>
	<div class="container">
		<h1>Il tuo Carrello</h1>

		<c:if test="${not empty sessionScope.errore}">
			<div class="error">${sessionScope.errore}</div>
			<c:remove var="errore" scope="session" />
		</c:if>

		<c:choose>
			<c:when test="${empty carrelloMap}">
				<div class="empty-cart">
					<p>Il tuo carrello è vuoto.</p>
					<a href="${pageContext.request.contextPath}/home" class="btn">Continua
						lo shopping</a>
				</div>
			</c:when>

			<c:otherwise>
				<table>
					<thead>
						<tr>
							<th>Prodotto</th>
							<th>Prezzo</th>
							<th>Quantità</th>
							<th>Totale</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="entry" items="${carrelloMap}">
							<c:set var="voce" value="${entry.key}" />
							<c:set var="prodotto" value="${entry.value}" />
							<c:set var="totaleProdotto"
								value="${prodotto.prezzo * voce.quantita}" />
							<tr>
								<td><strong>${prodotto.nome}</strong></td>
								<td class="price">€ ${String.format("%.2f", prodotto.prezzo)}</td>
								<td>
									<form action="${pageContext.request.contextPath}/carrello"
										method="POST" style="margin: 0">
										<input type="hidden" name="action" value="add"> <input
											type="hidden" name="prodottoId" value="${prodotto.id}">
										<input type="number" name="quantita" value="${voce.quantita}"
											min="1" max="99" class="quantity"
											onchange="this.form.submit()">
									</form>
								</td>
								<td class="price">€ ${String.format("%.2f", totaleProdotto)}</td>
								<td>
									<form action="${pageContext.request.contextPath}/carrello"
										method="POST" style="margin: 0">
										<input type="hidden" name="action" value="remove"> <input
											type="hidden" name="prodottoId" value="${prodotto.id}">
										<button type="submit" class="btn-danger"
											style="padding: 5px 10px; font-size: 12px;">Rimuovi</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<div class="total">
					<p>Subtotale: € ${String.format("%.2f", subtotale)}</p>
					<p>Spedizione: € ${String.format("%.2f", speseSpedizione)}</p>
					<p style="font-size: 24px;">Totale: € ${String.format("%.2f", totaleFinale)}</p>
				</div>

				<div class="actions">
					<a href="${pageContext.request.contextPath}/home"
						class="btn btn-secondary">Continua lo shopping</a>
					<form action="${pageContext.request.contextPath}/carrello"
						method="POST" style="margin: 0">
						<input type="hidden" name="action" value="clear">
						<button type="submit" class="btn btn-secondary"
							onclick="return confirm('Svuotare il carrello?')">Svuota
							carrello</button>
					</form>
					<a href="${pageContext.request.contextPath}/checkout" class="btn">Procedi
						al checkout →</a>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>