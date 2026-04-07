<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Accesso Amministratore | CarryCrew</title>

<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon-16x16.png">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/admin-login.css">
</head>
<body>

	<div class="login-wrapper">
		<div class="login-header">
			<i class="fas fa-lock"></i>
			<h1>CarryCrew</h1>
			<p>Area di Amministrazione</p>
		</div>

		<div id="error-message"></div>

		<c:if test="${not empty errorMessage}">
			<div class="server-error">
				<c:out value="${errorMessage}" />
			</div>
		</c:if>

		<form id="adminLoginForm"
			action="${pageContext.request.contextPath}/admin/login" method="POST">

			<div class="form-group">
				<label for="email">Indirizzo Email</label> <input type="email"
					id="email" name="email" placeholder="admin@carrycrew.it"
					autocomplete="email">
			</div>

			<div class="form-group">
				<label for="password">Password</label>
				<div class="password-wrapper">
					<input type="password" id="password" name="password"
						placeholder="Inserisci la tua password"
						autocomplete="current-password"> <i class="fas fa-eye"
						id="togglePassword" title="Mostra/Nascondi password"></i>
				</div>
			</div>

			<button type="submit" class="btn-login">Accedi al pannello</button>

		</form>
	</div>

	<script src="${pageContext.request.contextPath}/scripts/admin-login.js"></script>

</body>
</html>