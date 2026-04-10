<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><c:choose>
		<c:when test="${categoria.id == 0}">Nuova Categoria</c:when>
		<c:otherwise>Categoria #${categoria.id} | CarryCrew Admin</c:otherwise>
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
	href="${pageContext.request.contextPath}/styles/admin-dettaglio-categoria.css">
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

			<div class="category-detail-container">

				<form id="categoryForm"
					action="${pageContext.request.contextPath}/admin/categoria"
					method="POST">

					<input type="hidden" name="id" value="${categoria.id}">

					<div class="category-header">
						<h1>
							<c:choose>
								<c:when test="${categoria.id == 0}">Crea Nuova Categoria</c:when>
								<c:otherwise>Dettaglio Categoria: #${categoria.id}</c:otherwise>
							</c:choose>
						</h1>
					</div>

					<div class="form-group">
						<label for="nome">Nome Categoria</label> <input type="text"
							id="nome" name="nome" value="<c:out value='${categoria.nome}' />"
							class="form-input"
							placeholder="Inserisci il nome della categoria...">
						<div id="nomeVuoto" class="alert-error"
							style="display: none; margin-top: 10px">
							<i class="fas fa-exclamation-triangle"></i> Il nome della
							categoria non può essere vuoto.
						</div>
					</div>

					<div class="form-group">
						<label for="descrizione">Descrizione</label>
						<textarea id="descrizione" name="descrizione" class="form-input"
							placeholder="Inserisci una breve descrizione della categoria..."><c:out
								value='${categoria.descrizione}' /></textarea>
					</div>

					<!-- ACTION BUTTONS - allineato allo stile cliente -->
					<div class="action-buttons">

						<c:if test="${categoria.id != 0}">
							<button type="submit" name="azione" value="elimina"
								class="btn-secondary btn-elimina"
								onclick="return confirm('Sei sicuro di voler eliminare questa categoria?');">
								Elimina Categoria</button>
						</c:if>

						<a href="${pageContext.request.contextPath}/admin/categorie"
							class="btn-secondary">Annulla</a>

						<button type="submit" class="btn-primary">
							<c:choose>
								<c:when test="${categoria.id == 0}">Crea Categoria</c:when>
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
		src="${pageContext.request.contextPath}/scripts/admin-dettaglio-categoria.js"></script>

</body>
</html>