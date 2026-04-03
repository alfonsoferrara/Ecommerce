<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestisci Indirizzi - Il Mio Ecommerce</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
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
        .address-card {
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 15px;
            background-color: #f8f9fa;
        }
        .address-info {
            margin-bottom: 10px;
        }
        .btn {
            display: inline-block;
            padding: 8px 15px;
            background-color: #ff6b6b;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-size: 12px;
        }
        .btn-danger {
            background-color: #dc3545;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .form-add {
            margin-top: 30px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background-color: #f8f9fa;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        .row {
            display: flex;
            gap: 15px;
        }
        .row > div {
            flex: 1;
        }
        .empty {
            text-align: center;
            padding: 30px;
            color: #666;
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
        .button-group {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
        .disabled-msg {
            font-size: 12px;
            color: #999;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📍 Gestisci Indirizzi</h1>
        
        <c:if test="${not empty sessionScope.messaggio}">
            <div class="message">${sessionScope.messaggio}</div>
            <c:remove var="messaggio" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.errore}">
            <div class="error">${sessionScope.errore}</div>
            <c:remove var="errore" scope="session" />
        </c:if>
        
        <!-- Lista indirizzi esistenti -->
        <c:choose>
            <c:when test="${empty indirizzi}">
                <div class="empty">
                    <p>Non hai ancora salvato nessun indirizzo.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="indirizzo" items="${indirizzi}">
                    <div class="address-card">
                        <div class="address-info">
                            <strong>${indirizzo.via} ${indirizzo.civico}</strong>
                        </div>
                        <div class="address-info">
                            ${indirizzo.citta} (${indirizzo.provincia}) - ${indirizzo.cap}
                        </div>
                        <div class="button-group">
                            <form action="${pageContext.request.contextPath}/user/indirizzi" method="POST" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="indirizzoId" value="${indirizzo.id}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Cancellare questo indirizzo?')">🗑️ Elimina</button>
                            </form>
                        </div>
                        <div class="disabled-msg">
                            ⚠️ Nota: gli indirizzi non possono essere modificati per preservare la cronologia degli ordini. Se necessario, aggiungi un nuovo indirizzo.
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        
        <!-- Form per aggiungere nuovo indirizzo -->
        <div class="form-add">
            <h3>➕ Aggiungi nuovo indirizzo</h3>
            <form action="${pageContext.request.contextPath}/user/indirizzo" method="POST">
                <div class="row">
                    <div class="form-group">
                        <label>Via *</label>
                        <input type="text" name="via" required>
                    </div>
                    <div class="form-group">
                        <label>Civico *</label>
                        <input type="text" name="civico" required>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group">
                        <label>Città *</label>
                        <input type="text" name="citta" required>
                    </div>
                    <div class="form-group">
                        <label>Provincia *</label>
                        <input type="text" name="provincia" maxlength="2" placeholder="es. RM" required>
                    </div>
                    <div class="form-group">
                        <label>CAP *</label>
                        <input type="text" name="cap" maxlength="5" placeholder="es. 00100" required>
                    </div>
                </div>
                <button type="submit" class="btn">Salva indirizzo</button>
            </form>
        </div>
        
        <div>
            <a href="${pageContext.request.contextPath}/user/profilo" class="back-link">← Torna al profilo</a>
        </div>
    </div>
</body>
</html>