<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${prodotto.nome}</title>
</head>
<body>
	<c:forEach var="immagine" items="${immagini}">
		<img src="${pageContext.request.contextPath}${immagine.url}">
	</c:forEach>

	<h1>${prodotto.nome}</h1>
	<h3>Caratteristiche:</h3>

	<c:forEach var="entry" items="${specifiche}">
		<p>
			<strong>${entry.key}:</strong> ${entry.value}
		</p>
	</c:forEach>


	<p>Prezzo: € ${prodotto.prezzo}</p>

	<form action="${pageContext.request.contextPath}/carrello"
		method="POST">
		<input type="hidden" name="action" value="add"> <input
			type="hidden" name="prodottoId" value="${prodotto.id}"> <input
			type="hidden" name="quantita" value="1">
		<button type="submit">Aggiungi al carrello</button>
	</form>

	<br>
	<a href="${pageContext.request.contextPath}/carrello">Vai al
		carrello</a>

</body>
</html>