<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ERRORE 404</title>
</head>
<body>
<h1>404 Error - La pagina che cerchi non esiste!</h1>
    <p>Messaggio: <%= request.getAttribute("jakarta.servlet.error.message") %></p>
<button><a href="${pageContext.request.contextPath}/home">Torna alla home</a></button>
</body>
</html>