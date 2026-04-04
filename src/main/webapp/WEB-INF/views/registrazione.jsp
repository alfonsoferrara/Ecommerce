<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Registrati - CarryCrew</title>
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/registrazione.css">
</head>
<body>

	<jsp:include page="fragments/header.jsp" />

	<main class="register-container">
		<div class="register-card">

			<div class="register-header">
				<h1>Crea un Account</h1>
				<p>Inserisci i tuoi dati per registrarti</p>
			</div>

			<c:if test="${not empty errore}">
				<div class="alert-error">
					<i class="fas fa-exclamation-circle"></i> ${errore}
				</div>
			</c:if>

			<form action="${pageContext.request.contextPath}/registrazione"
				method="POST" id="registerForm" novalidate>

				<div class="form-row">
					<div class="form-group">
						<label for="nome">Nome</label> <input type="text" id="nome"
							name="nome" class="form-control" placeholder="es. Mario">
						<div class="error-message"></div>
					</div>
					<div class="form-group">
						<label for="cognome">Cognome</label> <input type="text"
							id="cognome" name="cognome" class="form-control"
							placeholder="es. Rossi">
						<div class="error-message"></div>
					</div>
				</div>

				<div class="form-group">
					<label for="email">Indirizzo Email</label> <input type="email"
						id="email" name="email" class="form-control"
						placeholder="es. mario.rossi@email.com">
					<div class="error-message"></div>
				</div>

				<div class="form-group">
					<label for="telefono">Numero di Telefono</label> <input type="tel"
						id="telefono" name="telefono" class="form-control"
						placeholder="es. 3331234567">
					<div class="error-message"></div>
				</div>

				<div class="form-group">
					<label for="password">Password</label> <input type="password"
						id="password" name="password" class="form-control"
						placeholder="Crea una password sicura"> <i
						class="fas fa-eye password-toggle" id="togglePassword"></i>
					<div class="error-message"></div>
				</div>

				<button type="submit" class="btn-block">
					Registrati <i class="fas fa-user-plus" style="margin-left: 8px;"></i>
				</button>
			</form>

			<div class="auth-links">
				Hai già un account? <a
					href="${pageContext.request.contextPath}/login">Accedi qui!</a>
			</div>

		</div>
	</main>

	<jsp:include page="fragments/footer.jsp" />

	<script
		src="${pageContext.request.contextPath}/scripts/registrazione.js"></script>

</body>
</html>