<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifica Profilo - Il Mio Ecommerce</title>
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
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
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
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
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
        .message {
            background-color: #d4edda;
            color: #155724;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
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
        hr {
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>👤 Modifica Profilo</h1>
        
        <c:if test="${not empty sessionScope.messaggio}">
            <div class="message">${sessionScope.messaggio}</div>
            <c:remove var="messaggio" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.errore}">
            <div class="error">${sessionScope.errore}</div>
            <c:remove var="errore" scope="session" />
        </c:if>
        
        <form action="${pageContext.request.contextPath}/user/informazioni" method="POST">
            <div class="form-group">
                <label>Nome *</label>
                <input type="text" name="nome" value="${cliente.nome}" required>
            </div>
            
            <div class="form-group">
                <label>Cognome *</label>
                <input type="text" name="cognome" value="${cliente.cognome}" required>
            </div>
            
            <div class="form-group">
                <label>Email *</label>
                <input type="email" name="email" value="${cliente.email}" required>
            </div>
            
            <div class="form-group">
                <label>Telefono</label>
                <input type="tel" name="telefono" value="${cliente.telefono}">
            </div>
            
            <hr>
            
            <div class="form-group">
                <label>Nuova Password (lasciare vuoto per mantenere quella attuale)</label>
                <input type="password" name="password" minlength="6">
            </div>
            
            <div class="buttons">
                <button type="submit" class="btn">Salva modifiche</button>
                <a href="${pageContext.request.contextPath}/user/profilo" class="btn btn-secondary">Annulla</a>
            </div>
        </form>
        
        <div style="margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/user/profilo" class="back-link">← Torna al profilo</a>
        </div>
    </div>
</body>
</html>