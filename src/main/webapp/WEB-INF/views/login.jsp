<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="login" method="POST">
	<fieldset>
	${errore}<br>
		<legend>Inserisci i tuoi dati</legend>
		Email:<input type="email" name="email" required>
		Password:<input type="password" name="password" required>
	</fieldset>
	<input type="submit" value="Accedi">
</form>

<a href="registrazione" style="text-decoration:underline">Non hai un account? Registrati!</a>
</body>
</html>