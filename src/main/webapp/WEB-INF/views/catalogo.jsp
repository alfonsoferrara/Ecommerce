<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>${catNome}</h1>
	<h3>${catDesc}</h3>
	<c:choose>
		<c:when test="${empty prodotti}">
			<p>Nessun prodotto disponibile.</p>
		</c:when>
		<c:otherwise>
			<c:forEach var="prodotto" items="${prodotti}">
				<h4>${prodotto.nome}</h4>
				<p>€ ${prodotto.prezzo}</p>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>