<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Carrello - CarryCrew</title>
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/carrello.css">

</head>
<body>

	<jsp:include page="fragments/header.jsp" />

	<div class="cart-wrapper">
		<div class="cart-header">
			<h1>
				<i class="fas fa-shopping-cart"></i> Il tuo Carrello
			</h1>
		</div>

		<c:if test="${not empty sessionScope.erroreAggiunta}">
			<div class="alert-error">
				<i class="fas fa-exclamation-triangle"></i> <span>${sessionScope.erroreAggiunta}</span>
			</div>
			<c:remove var="erroreAggiunta" scope="session" />
		</c:if>

		<c:choose>
			<c:when test="${empty carrelloMap}">
				<div class="cart-items-section">
					<div class="empty-cart">
						<i class="fas fa-box-open"></i>
						<p>Il tuo carrello è attualmente vuoto.</p>
						<a href="${pageContext.request.contextPath}/home" class="btn"
							style="max-width: 300px;"> <i class="fas fa-arrow-left"></i>
							Torna allo Shopping
						</a>
					</div>
				</div>
			</c:when>

			<c:otherwise>
				<div class="cart-container">

					<div class="cart-items-section">
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

									<tr
										class="${prodotto.id == sessionScope.erroreAggiuntaProdottoId ? 'row-error' : ''}">
										<td><span class="product-name">${prodotto.nome}</span></td>
										<td class="price">€ ${String.format("%.2f", prodotto.prezzo)}</td>
										<td>
											<form action="${pageContext.request.contextPath}/carrello"
												method="POST" style="margin: 0;">
												<input type="hidden" name="action" value="update"> <input
													type="hidden" name="prodottoId" value="${prodotto.id}">
												<input type="number" name="quantita"
													value="${voce.quantita}" min="1" max="99"
													class="quantity-input" onchange="this.form.submit()">
											</form>
										</td>
										<td class="price">€ ${String.format("%.2f", totaleProdotto)}</td>
										<td style="text-align: center;">
											<form action="${pageContext.request.contextPath}/carrello"
												method="POST" style="margin: 0;"
												onsubmit="return confirm('Rimuovere ${prodotto.nome} dal carrello?');">
												<input type="hidden" name="action" value="remove"> <input
													type="hidden" name="prodottoId" value="${prodotto.id}">
												<button type="submit" class="btn-danger-icon"
													title="Rimuovi prodotto">
													<i class="fas fa-trash-alt"></i>
												</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

						<c:remove var="erroreAggiuntaProdottoId" scope="session" />

						<div
							style="margin-top: 20px; display: flex; justify-content: flex-end;">
							<form action="${pageContext.request.contextPath}/carrello"
								method="POST" style="margin: 0;">
								<input type="hidden" name="action" value="clear">
								<button type="submit" class="btn-danger-icon"
									style="font-size: 14px; text-decoration: underline;"
									onclick="return confirm('Sei sicuro di voler svuotare l\'intero carrello?')">
									<i class="fas fa-times"></i> Svuota carrello
								</button>
							</form>
						</div>
					</div>

					<div class="cart-summary-section">
						<h3 class="summary-title">Riepilogo Ordine</h3>

						<div class="summary-row">
							<span>Subtotale</span> <span>€ ${String.format("%.2f", subtotale)}</span>
						</div>

						<div class="summary-row">
							<span>Spedizione</span>
							<c:choose>
								<c:when test="${speseSpedizione == 0}">
									<span style="color: #28a745; font-weight: bold;">Gratuita</span>
								</c:when>
								<c:otherwise>
									<span>€ ${String.format("%.2f", speseSpedizione)}</span>
								</c:otherwise>
							</c:choose>
						</div>

						<c:if test="${speseSpedizione > 0}">
							<div
								style="font-size: 12px; color: #666; margin-bottom: 15px; text-align: right;">
								Spedizione gratuita sopra i 50€</div>
						</c:if>

						<div class="summary-row summary-total">
							<span>Totale</span> <span>€ ${String.format("%.2f", totaleFinale)}</span>
						</div>

						<div style="margin-top: 30px;">
							<a href="${pageContext.request.contextPath}/checkout" class="btn">
								Procedi al Checkout <i class="fas fa-chevron-right"
								style="margin-left: 8px;"></i>
							</a> <a href="${pageContext.request.contextPath}/home"
								class="btn btn-outline"> <i class="fas fa-arrow-left"
								style="margin-right: 8px;"></i> Continua lo shopping
							</a>
						</div>
					</div>

				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<jsp:include page="fragments/footer.jsp" />

</body>
</html>