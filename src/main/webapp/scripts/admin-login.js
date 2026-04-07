document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("adminLoginForm");
    const errorMessage = document.getElementById("error-message");

    // Elementi per l'occhio della password
    const togglePassword = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");

    // 1. LOGICA MOSTRA/NASCONDI PASSWORD
    if (togglePassword && passwordInput) {
        togglePassword.addEventListener("click", function() {
            // Cambia il tipo di input da 'password' a 'text' e viceversa
            const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
            passwordInput.setAttribute("type", type);
			this.classList.toggle('fa-eye');
			this.classList.toggle('fa-eye-slash');
        });
    }

    // 2. LOGICA DI VALIDAZIONE CON REGEX
    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            const email = document.getElementById("email").value.trim();
            const password = passwordInput.value.trim();

            // Regex per un indirizzo email valido (formato standard)
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            // Regex per la password: almeno 6 caratteri (qualsiasi carattere)
            const passwordRegex = /^.{6,}$/;

            let errorText = "";

            // Controlli tramite test() della Regex
            if (!emailRegex.test(email)) {
                errorText = "Inserisci un indirizzo email valido.";
            } else if (!passwordRegex.test(password)) {
                errorText = "La password deve contenere almeno 6 caratteri.";
            }

            if (errorText !== "") {
                // Blocca l'invio del form
                event.preventDefault();

                // Mostra l'errore
                errorMessage.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${errorText}`;
                errorMessage.style.display = "block";

                // Nasconde eventuali errori del server
                const serverError = document.querySelector('.server-error');
                if (serverError) {
                    serverError.style.display = 'none';
                }
            } else {
                errorMessage.style.display = "none";
            }
        });
    }
});