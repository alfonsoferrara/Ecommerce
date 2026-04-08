<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dettaglio Prodotto #${prodotto.id} | CarryCrew Admin</title>

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
	href="${pageContext.request.contextPath}/styles/admin-dettaglio-prodotto.css">
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

			<div class="product-detail-container">

				<form id="mainUpdateForm"
					action="${pageContext.request.contextPath}/admin/prodotto"
					method="POST" enctype="multipart/form-data">

					<input type="hidden" name="id" value="${prodotto.id}"> <input
						type="hidden" name="id" value="${prodotto.id}">

					<div class="product-header">
						<h1>
							<c:choose>
								<c:when test="${prodotto.id == 0}">Nuovo Prodotto</c:when>
								<c:otherwise>Prodotto: #${prodotto.id}</c:otherwise>
							</c:choose>
						</h1>

						<c:if test="${prodotto.id != 0}">
							<div class="status-controls">
								<select name="attivo">
									<option value="true" ${prodotto.attivo ? 'selected' : ''}>Stato:
										Attivo</option>
									<option value="false" ${!prodotto.attivo ? 'selected' : ''}>Stato:
										Disattivo</option>
								</select>
								<button type="submit" class="btn-primary">Aggiorna</button>
							</div>
						</c:if>
					</div>

					<div class="form-group">
						<label>Nome Prodotto</label> <input type="text" name="nome"
							value="<c:out value='${prodotto.nome}' />" class="form-input"
							required>
					</div>

					<div class="product-grid">

						<div>
							<label
								style="font-size: 0.85rem; font-weight: 700; color: #aaa; text-transform: uppercase; margin-bottom: 8px; display: block;">Descrizione</label>
							<textarea name="descrizione" class="form-input"><c:out
									value='${prodotto.descrizione}' /></textarea>
						</div>

						<div>
							<div class="form-group">
								<label>Prezzo (€)</label> <input type="number" step="0.01"
									name="prezzo" value="${prodotto.prezzo}" class="form-input"
									required>
							</div>

							<div class="form-group">
								<label>Categoria di Appartenenza</label> <select
									name="categoriaId" class="form-input">
									<c:forEach items="${tutteCategorie}" var="cat">
										<option value="${cat.id}"
											${cat.id == prodotto.categoriaId ? 'selected' : ''}>
											${cat.nome}</option>
									</c:forEach>
								</select>
							</div>

							<div class="form-group">
								<label>Stock Disponibile</label> <input type="number"
									name="stock" value="${prodotto.stock}" class="form-input"
									required>
							</div>
						</div>

					</div>

					<h3 class="section-title">Immagini Prodotto</h3>
					<div class="image-gallery">
						<c:forEach items="${immagini}" var="img">
							<div class="image-card">
								<c:if test="${img.principal}">
									<span class="principal-badge">Principale</span>
								</c:if>
								<img src="${pageContext.request.contextPath}/${img.url}"
									alt="Immagine Prodotto">

								<div class="image-actions">
									<c:if test="${!img.principal}">
										<button type="button" class="btn-secondary"
											onclick="setPrincipalImage(${img.id})">Imposta
											Principale</button>
									</c:if>
									<button type="button" class="btn-danger"
										onclick="deleteImage(${img.id})">Elimina</button>
								</div>
							</div>
						</c:forEach>
					</div>

					<div class="form-group" style="margin-bottom: 40px;">
						<label>Aggiungi Nuove Immagini</label> <input type="file"
							name="nuoveImmagini" multiple accept="image/*" class="form-input"
							style="padding: 9px;">
					</div>

					<h3 class="section-title">Caratteristiche e Valori</h3>

					<div class="specs-list">
						<c:forEach items="${specifiche}" var="entry">
							<div class="spec-item">
								<label>${entry.key}</label> <input type="text"
									name="spec_${entry.key}"
									value="<c:out value='${entry.value}' />" class="form-input"
									style="margin-bottom: 0;">
								<button type="button" class="btn-danger"
									onclick="deleteSpec('<c:out value="${entry.key}" />')"
									title="Elimina Caratteristica">
									<i class="fas fa-trash"></i>
								</button>
							</div>
						</c:forEach>
						<c:if test="${empty specifiche}">
							<p style="color: #666; font-style: italic;">Nessuna
								caratteristica salvata per questo prodotto.</p>
						</c:if>
					</div>

					<div id="new-specs-container">
						<div class="spec-item spec-new-row">
							<select name="nuovoAttributo" class="form-input"
								style="width: 200px; margin-bottom: 0;">
								<option value="">-- Seleziona Attributo --</option>
								<c:forEach items="${tuttiAttributi}" var="attr">
									<option value="${attr.nome}">${attr.nome}</option>
								</c:forEach>
							</select> <input type="text" name="nuovoValore"
								placeholder="Inserisci il valore..." class="form-input"
								style="margin-bottom: 0;">
						</div>
					</div>

					<button type="button" id="btnAddSpec" class="btn-secondary"
						style="margin-bottom: 40px;">
						<i class="fas fa-plus"></i> Aggiungi un'altra caratteristica
					</button>

					<div
						style="display: flex; justify-content: flex-end; gap: 15px; border-top: 1px solid #222; padding-top: 20px;">

						<a href="${pageContext.request.contextPath}/admin/prodotti"
							class="btn-secondary"
							style="font-size: 1.1rem; padding: 15px 40px; text-decoration: none; display: flex; align-items: center;">Annulla</a>

						<button type="submit" class="btn-primary"
							style="font-size: 1.1rem; padding: 15px 40px;">
							<c:choose>
								<c:when test="${prodotto.id == 0}">Crea Prodotto</c:when>
								<c:otherwise>Salva Modifiche</c:otherwise>
							</c:choose>
						</button>

					</div>

				</form>
			</div>

			<form id="formSetPrincipal"
				action="${pageContext.request.contextPath}/admin/prodotto"
				method="POST" style="display: none;">
				<input type="hidden" name="id" value="${prodotto.id}"> <input
					type="hidden" name="action" value="setPrincipalImage"> <input
					type="hidden" name="immagineId" id="hiddenPrincipalImgId">
			</form>

			<form id="formDeleteImage"
				action="${pageContext.request.contextPath}/admin/prodotto"
				method="POST" style="display: none;">
				<input type="hidden" name="id" value="${prodotto.id}"> <input
					type="hidden" name="action" value="deleteImage"> <input
					type="hidden" name="immagineId" id="hiddenDeleteImgId">
			</form>

			<form id="formDeleteSpec"
				action="${pageContext.request.contextPath}/admin/prodotto"
				method="POST" style="display: none;">
				<input type="hidden" name="id" value="${prodotto.id}"> <input
					type="hidden" name="action" value="deleteCharacteristic"> <input
					type="hidden" name="nomeCaratteristica" id="hiddenDeleteSpecName">
			</form>

		</div>
	</main>

	<script
		src="${pageContext.request.contextPath}/scripts/admin-dashboard.js"></script>
	<script
		src="${pageContext.request.contextPath}/scripts/admin-dettaglio-prodotto.js"></script>

</body>
</html>