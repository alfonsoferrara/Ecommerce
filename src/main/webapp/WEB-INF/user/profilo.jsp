<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Il mio Profilo - Il Mio Ecommerce</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background-color: #f5f5f5;
}

.container {
	max-width: 1000px;
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
	margin-bottom: 30px;
}

.welcome {
	font-size: 18px;
	color: #666;
	margin-bottom: 30px;
}

.box-container {
	display: flex;
	gap: 30px;
	justify-content: center;
	flex-wrap: wrap;
}

.box {
	flex: 1;
	min-width: 200px;
	background-color: #f8f9fa;
	border-radius: 10px;
	padding: 30px 20px;
	text-align: center;
	transition: transform 0.3s, box-shadow 0.3s;
	border: 1px solid #ddd;
}

.box:hover {
	transform: translateY(-5px);
	box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
}

.box-icon {
	font-size: 48px;
	margin-bottom: 15px;
}

.box-title {
	font-size: 20px;
	font-weight: bold;
	color: #333;
	margin-bottom: 10px;
}

.box-description {
	font-size: 14px;
	color: #666;
	margin-bottom: 20px;
}

.box-link {
	display: inline-block;
	padding: 10px 20px;
	background-color: #ff6b6b;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	transition: background-color 0.3s;
}

.box-link:hover {
	background-color: #ff5252;
}

.logout {
	text-align: center;
	margin-top: 40px;
	padding-top: 20px;
	border-top: 1px solid #ddd;
}

.logout-link {
	color: #dc3545;
	text-decoration: none;
	font-size: 14px;
}

.logout-link:hover {
	text-decoration: underline;
}
</style>
</head>
<body>
	<div class="container">
		<h1>Il mio Profilo</h1>

		<div class="welcome">
			Ciao, <strong>${utenteLoggato.nome} ${utenteLoggato.cognome}</strong>!
			Benvenuto nella tua area personale.
		</div>

		<div class="box-container">
			<!-- Box 1: Ordini -->
			<div class="box">
				<div class="box-icon">📦</div>
				<div class="box-title">I miei Ordini</div>
				<div class="box-description">Visualizza lo storico dei tuoi
					ordini</div>
				<a href="${pageContext.request.contextPath}/user/ordini"
					class="box-link"> Vedi ordini → </a>
			</div>

			<!-- Box 2: Indirizzi -->
			<div class="box">
				<div class="box-icon">📍</div>
				<div class="box-title">Gestisci Indirizzi</div>
				<div class="box-description">Aggiungi o modifica i tuoi
					indirizzi di spedizione</div>
				<a href="${pageContext.request.contextPath}/user/indirizzi"
					class="box-link"> Gestisci indirizzi → </a>
			</div>

			<!-- Box 3: Profilo -->
			<div class="box">
				<div class="box-icon">👤</div>
				<div class="box-title">Modifica Profilo</div>
				<div class="box-description">Aggiorna i tuoi dati personali</div>
				<a href="${pageContext.request.contextPath}/user/informazioni"
					class="box-link"> Modifica profilo → </a>
			</div>
		</div>

		<div class="logout">
			<a href="${pageContext.request.contextPath}/logout"
				class="logout-link"> 🚪 Esci dal tuo account </a>
		</div>
	</div>
</body>
</html>