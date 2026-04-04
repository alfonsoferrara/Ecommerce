<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>404 Pagina Non Trovata | CarryCrew</title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/error.css">
</head>
<body>

	<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

	<main class="error-container">
		<h1 class="error-code">404</h1>
		<h2 class="error-title">Pagina non trovata</h2>

		<div class="error-message">
			<p>Oops! Sembra che tu ti sia spinto troppo oltre. La pagina che
				cerchi non esiste, è stata rimossa o ha cambiato indirizzo.</p>

			<%-- Stampiamo il dettaglio tecnico dell'errore solo se presente --%>
			<%
			if (request.getAttribute("jakarta.servlet.error.message") != null) {
			%>
			<div class="error-detail">
				<i class="fas fa-exclamation-triangle"></i> Messaggio di sistema:
				<%=request.getAttribute("jakarta.servlet.error.message")%>
			</div>
			<%
			}
			%>
		</div>

		<a href="${pageContext.request.contextPath}/home" class="btn-error">Torna
			alla home</a>
	</main>

	<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />

	<script src="${pageContext.request.contextPath}/scripts/main.js"></script>

</body>
</html>