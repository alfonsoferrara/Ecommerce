<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrati</title>
</head>
<body>
<form action="registrazione" method="POST">
	<fieldset>
	${errore}<br>
		<legend>Inserisci i tuoi dati</legend>
		Nome:<input type="text" name="nome" required>
		Cognome:<input type="text" name="cognome" required>
		Email:<input type="email" name="email" required>
		Password:<input type="password" name="password" required>
		Telefono:<input type="tel" name="telefono" required>
	</fieldset>
	<input type="submit" value="Registrati">
</form>
</body>
</html>