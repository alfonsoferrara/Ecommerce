<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Accedi - CarryCrew</title>
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/login.css">
</head>
<body>

	<jsp:include page="fragments/header.jsp" />

	<main class="login-container">
		<div class="login-card">

			<div class="login-header">
				<h1>Bentornato</h1>
				<p>Inserisci i tuoi dati per accedere</p>
			</div>

			<c:if test="${not empty errore}">
				<div class="alert-error">
					<i class="fas fa-exclamation-circle"></i> ${errore}
				</div>
			</c:if>

			<form action="${pageContext.request.contextPath}/login" method="POST"
				id="loginForm" novalidate>

				<div class="form-group">
					<label for="email">Indirizzo Email</label> <input type="email"
						id="email" name="email" class="form-control"
						placeholder="es. mario.rossi@email.com">
					<div class="error-message"></div>
				</div>

				<div class="form-group">
					<label for="password">Password</label> <input type="password"
						id="password" name="password" class="form-control"
						placeholder="Inserisci la password"> <i
						class="fas fa-eye password-toggle" id="togglePassword"></i>
					<div class="error-message"></div>
				</div>

				<button type="submit" class="btn-block">
					Accedi <i class="fas fa-sign-in-alt" style="margin-left: 8px;"></i>
				</button>
			</form>

			<div class="auth-links">
				Non hai ancora un account? <a
					href="${pageContext.request.contextPath}/registrazione">Registrati
					ora!</a>
			</div>

		</div>
	</main>

	<jsp:include page="fragments/footer.jsp" />

	<script src="${pageContext.request.contextPath}/scripts/login.js"></script>

</body>
</html>