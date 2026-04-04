<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Nuova Recensione - Il Mio Ecommerce</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background-color: #f5f5f5;
}

.container {
	max-width: 600px;
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

.form-group {
	margin-bottom: 20px;
}

label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
}

input, select, textarea {
	width: 100%;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-sizing: border-box;
}

textarea {
	resize: vertical;
	min-height: 100px;
}

.btn {
	display: inline-block;
	padding: 12px 25px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	border: none;
	cursor: pointer;
	font-size: 16px;
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

.buttons {
	display: flex;
	gap: 15px;
	margin-top: 20px;
}

.error {
	background-color: #f8d7da;
	color: #721c24;
	padding: 10px;
	border-radius: 5px;
	margin-bottom: 20px;
}

.back-link {
	display: inline-block;
	margin-top: 20px;
	color: #ff6b6b;
	text-decoration: none;
}
</style>
</head>
<body>
	<div class="container">
		<h1>✏️ Nuova Recensione</h1>

		<c:if test="${not empty sessionScope.errore}">
			<div class="error">${sessionScope.errore}</div>
			<c:remove var="errore" scope="session" />
		</c:if>

		<form
			action="${pageContext.request.contextPath}/user/nuova-recensione"
			method="POST">
			<input type="hidden" name="prodottoId" value="${prodottoId}">

			<div class="form-group">
				<label>Titolo *</label> <input type="text" name="titolo"
					placeholder="Es: Ottimo prodotto!" required maxlength="100">
			</div>

			<div class="form-group">
				<label>Valutazione *</label> <select name="valutazione" required>
					<option value="">-- Seleziona --</option>
					<option value="1">1 ⭐</option>
					<option value="2">2 ⭐⭐</option>
					<option value="3">3 ⭐⭐⭐</option>
					<option value="4">4 ⭐⭐⭐⭐</option>
					<option value="5">5 ⭐⭐⭐⭐⭐</option>
				</select>
			</div>

			<div class="form-group">
				<label>Commento</label>
				<textarea name="commento"
					placeholder="Condividi la tua esperienza con questo prodotto..."></textarea>
			</div>

			<div class="buttons">
				<button type="submit" class="btn">Pubblica recensione</button>
				<a href="${pageContext.request.contextPath}/user/recensioni"
					class="btn btn-secondary">Annulla</a>
			</div>
		</form>

		<div>
			<a href="${pageContext.request.contextPath}/user/recensioni"
				class="back-link">← Torna alle recensioni</a>
		</div>
	</div>
</body>
</html>