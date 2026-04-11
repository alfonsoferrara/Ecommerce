<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><c:choose>
		<c:when test="${attributo.id == 0}">Nuovo Attributo</c:when>
		<c:otherwise>Attributo #${attributo.id} | CarryCrew Admin</c:otherwise>
	</c:choose></title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-dashboard.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-sidebar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-topbar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-dettaglio-attributo.css">
</head>
<body>

	<jsp:include page="/WEB-INF/admin/fragments/admin-sidebar.jsp" />

	<main class="admin-main-content">

		<jsp:include page="/WEB-INF/admin/fragments/admin-topbar.jsp" />

		<div class="dashboard-content">

			<c:if test="${not empty operazioneRiuscita}">
				<div class="alert-success">
					<i class="fas fa-check-circle"></i> ${operazioneRiuscita}
				</div>
			</c:if>
			
			<c:if test="${not empty errore}">
				<div class="alert-error">
					<i class="fas fa-exclamation-triangle"></i> ${errore}
				</div>
			</c:if>

			<div class="attribute-detail-container">

				<form id="attributoForm"
					action="${pageContext.request.contextPath}/admin/attributo"
					method="POST">

					<input type="hidden" name="id" value="${attributo.id}">

					<div class="attribute-header">
						<h1>
							<c:choose>
								<c:when test="${attributo.id == 0}">Crea Nuovo Attributo</c:when>
								<c:otherwise>Dettaglio Attributo: #${attributo.id}</c:otherwise>
							</c:choose>
						</h1>
					</div>

					<div class="form-group">
						<label for="nome">Nome Attributo (es. Colore, Taglia,
							Memoria)</label> <input type="text" id="nome" name="nome"
							value="<c:out value='${attributo.nome}' />" class="form-input"
							placeholder="Inserisci il nome...">
						<div id="nomeVuoto" class="alert-error"
							style="display: none; margin-top: 10px">
							<i class="fas fa-exclamation-triangle"></i> Il nome
							dell'attributo non può essere vuoto.
						</div>
					</div>

					<c:if test="${attributo.id != 0}">
						<div class="associated-products-section">
							<h3>
								<i class="fas fa-tags"></i> Valori Assunti e Prodotti Correlati
							</h3>

							<c:choose>
								<c:when test="${dettagliAttributo.hasProdotti()}">
									<div class="admin-table-wrapper">
										<table class="admin-table">
											<thead>
												<tr>
													<th style="width: 20%;">ID Prodotto</th>
													<th style="width: 50%;">Nome Prodotto</th>
													<th style="width: 30%;">Valore Assunto</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${dettagliAttributo.prodottiAssociati}"
													var="pa">
													<tr>
														<td><a
															href="${pageContext.request.contextPath}/admin/prodotto?id=${pa.prodottoId}"
															style="color: #fff; text-decoration: underline;">
																#${pa.prodottoId} </a></td>
														<td><strong>${pa.prodottoNome}</strong></td>
														<td><span style="color: #2ecc71; font-weight: bold;">${pa.valoreAttr}</span></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</c:when>
								<c:otherwise>
									<p style="color: #888; font-style: italic;">Questo
										attributo non è attualmente utilizzato da nessun prodotto nel
										catalogo.</p>
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>

					<div class="action-buttons">

						<c:if test="${attributo.id != 0}">
							<button type="submit" id="btnElimina" class="btn-danger"
								name="azione" value="elimina">
								<i class="fas fa-trash"></i> Elimina Attributo
							</button>
						</c:if>

						<a href="${pageContext.request.contextPath}/admin/attributi"
							class="btn-secondary">Annulla</a>

						<button type="submit" class="btn-primary">
							<c:choose>
								<c:when test="${attributo.id == 0}">Salva Nuovo Attributo</c:when>
								<c:otherwise>Salva Modifiche</c:otherwise>
							</c:choose>
						</button>
					</div>

				</form>
			</div>
		</div>
	</main>

	<script
		src="${pageContext.request.contextPath}/scripts/admin-dashboard.js"></script>
	<script
		src="${pageContext.request.contextPath}/scripts/admin-dettaglio-attributo.js"></script>

</body>
</html>