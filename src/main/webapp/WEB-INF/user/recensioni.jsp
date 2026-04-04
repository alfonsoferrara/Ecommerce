<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Le mie Recensioni - Il Mio Ecommerce</title>
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
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
	color: #333;
	border-bottom: 2px solid #ff6b6b;
	padding-bottom: 10px;
}

.review-card {
	border: 1px solid #ddd;
	border-radius: 10px;
	padding: 15px;
	margin-bottom: 20px;
	background-color: #f8f9fa;
}

.review-title {
	font-size: 18px;
	font-weight: bold;
	color: #333;
}

.review-rating {
	color: #ffc107;
	margin: 5px 0;
}

.review-comment {
	color: #666;
	margin: 10px 0;
}

.review-date {
	font-size: 12px;
	color: #999;
}

.btn {
	display: inline-block;
	padding: 10px 20px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	border: none;
	cursor: pointer;
	margin-bottom: 20px;
}

.btn:hover {
	background-color: #ff5252;
}

.empty {
	text-align: center;
	padding: 50px;
	color: #666;
}

.back-link {
	display: inline-block;
	margin-top: 20px;
	color: #ff6b6b;
	text-decoration: none;
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
</style>
</head>
<body>
	<div class="container">
		<h1>⭐ Le mie Recensioni</h1>

		<c:if test="${not empty sessionScope.messaggio}">
			<div class="message">${sessionScope.messaggio}</div>
			<c:remove var="messaggio" scope="session" />
		</c:if>

		<c:if test="${not empty sessionScope.errore}">
			<div class="error">${sessionScope.errore}</div>
			<c:remove var="errore" scope="session" />
		</c:if>

		<a
			href="${pageContext.request.contextPath}/user/nuova-recensione?prodottoId=1"
			class="btn">+ Nuova Recensione</a>

		<c:choose>
			<c:when test="${empty recensioni}">
				<div class="empty">
					<p>Non hai ancora scritto nessuna recensione.</p>
					<p>Clicca su "Nuova Recensione" per iniziare!</p>
				</div>
			</c:when>
			<c:otherwise>
				<c:forEach var="recensione" items="${recensioni}">
					<div class="review-card">
						<div class="review-title">${recensione.titolo}</div>
						<div class="review-rating">
							<c:forEach begin="1" end="${recensione.valutazione}">⭐</c:forEach>
							<c:forEach begin="${recensione.valutazione + 1}" end="5">☆</c:forEach>
						</div>
						<div class="review-comment">${recensione.commento}</div>
						<div class="review-date">${recensione.data}</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>

		<div>
			<a href="${pageContext.request.contextPath}/user/profilo"
				class="back-link">← Torna al profilo</a>
		</div>
	</div>
</body>
</html>