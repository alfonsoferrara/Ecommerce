<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${prodotto.nome}</title>
</head>
<body>
    <h1>${prodotto.nome}</h1>
    <p>Prezzo: € ${prodotto.prezzo}</p>
    
    <!-- Usa un FORM invece di un link diretto -->
    <form action="${pageContext.request.contextPath}/carrello" method="POST">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="prodottoId" value="${prodotto.id}">
        <input type="hidden" name="quantita" value="1">
        <button type="submit">Aggiungi al carrello</button>
    </form>
    
    <br>
    <a href="${pageContext.request.contextPath}/carrello">Vai al carrello</a>
</body>
</html>