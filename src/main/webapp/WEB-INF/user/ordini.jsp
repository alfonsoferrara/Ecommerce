<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>I miei Ordini - Il Mio Ecommerce</title>
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
	padding: 30px;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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

.status {
	display: inline-block;
	padding: 5px 10px;
	border-radius: 5px;
	font-size: 12px;
	font-weight: bold;
}

.status-IN_ELABORAZIONE {
	background-color: #fff3cd;
	color: #856404;
}

.status-SPEDITO {
	background-color: #d4edda;
	color: #155724;
}

.status-CONSEGNATO {
	background-color: #cce5ff;
	color: #004085;
}

.status-CANCELLATO {
	background-color: #f8d7da;
	color: #721c24;
}

.btn {
	display: inline-block;
	padding: 8px 15px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	font-size: 12px;
}

.btn:hover {
	background-color: #ff5252;
}

.empty {
	text-align: center;
	padding: 50px;
	color: #666;
}

.back-link {
	display: inline-block;
	margin-top: 20px;
	color: #ff6b6b;
	text-decoration: none;
}

.price {
	color: #ff6b6b;
	font-weight: bold;
}

/* Paginazione */
.pagination {
	display: flex;
	justify-content: center;
	margin-top: 30px;
	gap: 5px;
	flex-wrap: wrap;
}

.pagination a, .pagination span {
	display: inline-block;
	padding: 8px 12px;
	text-decoration: none;
	border: 1px solid #ddd;
	border-radius: 5px;
	color: #333;
	transition: background-color 0.3s;
}

.pagination a:hover {
	background-color: #ff6b6b;
	color: white;
	border-color: #ff6b6b;
}

.pagination .active {
	background-color: #ff6b6b;
	color: white;
	border-color: #ff6b6b;
}

.pagination .disabled {
	color: #ccc;
	cursor: not-allowed;
}

.info-pagina {
	text-align: center;
	margin-bottom: 20px;
	color: #666;
	font-size: 14px;
}
</style>
</head>
<body>
	<div class="container">
		<h1>📦 I miei Ordini</h1>

		<c:choose>
			<c:when test="${empty ordini}">
				<div class="empty">
					<p>Non hai ancora effettuato nessun ordine.</p>
					<a href="${pageContext.request.contextPath}/home" class="btn">Inizia
						lo shopping →</a>
				</div>
			</c:when>
			<c:otherwise>
				<!-- Info paginazione -->
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
					</thead>
					</thead>
					<tbody>
						<c:forEach var="ordine" items="${ordini}">
							<tr>
								<td><strong>#${ordine.id}</strong></td>
								<td>${ordine.data}</td>
								<td class="price">€ ${String.format("%.2f", ordine.totale)}</td>
								<td><span class="status status-${ordine.stato}">${ordine.stato}</span>
								</td>
								<td>${ordine.metodoPagamento}</td>
								<td><a
									href="${pageContext.request.contextPath}/riepilogo?id=${ordine.id}"
									class="btn">Dettagli</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<!-- Paginazione -->
				<c:if test="${totalePagine > 1}">
					<div class="pagination">
						<!-- Pulsante Prima pagina -->
						<c:choose>
							<c:when test="${paginaCorrente > 1}">
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=1">«
									Prima</a>
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=${paginaCorrente - 1}">‹
									Precedente</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">« Prima</span>
								<span class="disabled">‹ Precedente</span>
							</c:otherwise>
						</c:choose>

						<!-- Numeri pagina -->
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

						<!-- Pulsante Ultima pagina -->
						<c:choose>
							<c:when test="${paginaCorrente < totalePagine}">
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=${paginaCorrente + 1}">Successivo
									›</a>
								<a
									href="${pageContext.request.contextPath}/user/ordini?pagina=${totalePagine}">Ultima
									»</a>
							</c:when>
							<c:otherwise>
								<span class="disabled">Successivo ›</span>
								<span class="disabled">Ultima »</span>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
			</c:otherwise>
		</c:choose>

		<div style="margin-top: 20px;">
			<a href="${pageContext.request.contextPath}/user/profilo"
				class="back-link">← Torna al profilo</a>
		</div>
	</div>
</body>
</html>