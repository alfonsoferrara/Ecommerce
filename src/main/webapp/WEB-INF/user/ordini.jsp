<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>I miei ordini - CarryCrew</title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/ordiniUtente.css">
</head>
<body>

	<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

	<main class="orders-wrapper">
		<div class="orders-header">
			<h1>
				<i class="fas fa-box"></i> I miei Ordini
			</h1>
		</div>

		<c:choose>
			<c:when test="${empty ordini}">
				<div class="empty">
					<i class="fas fa-shopping-basket"
						style="font-size: 48px; color: #ccc; margin-bottom: 20px;"></i>
					<p>Non hai ancora effettuato nessun ordine.</p>
					<a href="${pageContext.request.contextPath}/home" class="btn">
						Inizia lo shopping <i class="fas fa-arrow-right"
						style="margin-left: 5px;"></i>
					</a>
				</div>
			</c:when>

			<c:otherwise>
				<div class="info-pagina">
					<c:set var="pageSize" value="12" />
					<c:set var="totalePagine" value="${totalePagine}" />
					<c:set var="paginaCorrente" value="${paginaCorrente}" />

					Mostrando <strong>${ordini.size()}</strong> di <strong>${totaleOrdini}</strong>
					ordini
					<c:if test="${totalePagine > 1}">
                        - Pagina <strong>${paginaCorrente}</strong> di <strong>${totalePagine}</strong>
					</c:if>
				</div>

				<table>
					<thead>
						<tr>
							<th>N° Ordine</th>
							<th>Data</th>
							<th>Totale</th>
							<th>Stato</th>
							<th>Metodo Pagamento</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="ordine" items="${ordini}">
							<tr class="clickable-row"
								data-href="${pageContext.request.contextPath}/riepilogo?id=${ordine.id}">
								<td><strong>#${ordine.id}</strong></td>
								<td>${ordine.data}</td>
								<td class="price">€ ${String.format("%.2f", ordine.totale)}</td>
								<td><span class="status status-${ordine.stato}">${ordine.stato}</span></td>
								<td>${ordine.metodoPagamento}</td>
								<td style="text-align: right;"><a
									href="${pageContext.request.contextPath}/riepilogo?id=${ordine.id}"
									class="btn">Dettagli</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<c:if test="${totalePagine > 1}">
					<div class="pagination">

						<c:choose>
							<c:when test="${paginaCorrente > 1}">
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=1"
									title="Prima Pagina">«</a>
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=${paginaCorrente - 1}">‹
									Precedente</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">«</span>
								<span class="disabled">‹ Precedente</span>
							</c:otherwise>
						</c:choose>

						<c:set var="inizio" value="${paginaCorrente - 2}" />
						<c:set var="fine" value="${paginaCorrente + 2}" />

						<c:if test="${inizio < 1}">
							<c:set var="fine" value="${fine + (1 - inizio)}" />
							<c:set var="inizio" value="1" />
						</c:if>
						<c:if test="${fine > totalePagine}">
							<c:set var="inizio" value="${inizio - (fine - totalePagine)}" />
							<c:set var="fine" value="${totalePagine}" />
						</c:if>
						<c:if test="${inizio < 1}">
							<c:set var="inizio" value="1" />
						</c:if>

						<c:forEach var="i" begin="${inizio}" end="${fine}">
							<c:choose>
								<c:when test="${i == paginaCorrente}">
									<span class="active">${i}</span>
								</c:when>
								<c:otherwise>
									<a
										href="${pageContext.request.contextPath}/user/ordini?pagina=${i}">${i}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:choose>
							<c:when test="${paginaCorrente < totalePagine}">
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=${paginaCorrente + 1}">Successivo
									›</a>
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=${totalePagine}"
									title="Ultima Pagina">»</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">Successivo ›</span>
								<span class="disabled">»</span>
							</c:otherwise>
						</c:choose>

					</div>
				</c:if>
			</c:otherwise>
		</c:choose>

		<div style="margin-top: 30px;">
			<a href="${pageContext.request.contextPath}/user/profilo"
				class="back-link"> <i class="fas fa-arrow-left"></i> Torna al
				profilo
			</a>
		</div>
	</main>

	<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />

	<script src="${pageContext.request.contextPath}/scripts/ordini.js"></script>

</body>
</html>