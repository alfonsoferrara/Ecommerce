document.addEventListener("DOMContentLoaded", function() {

    const teamForm = document.getElementById("adminTeamForm");
    const btnElimina = document.getElementById("btnElimina");
    const inputAzione = document.getElementById("azioneInput");
    const emailInput = document.getElementById("email");
    const passInput = document.getElementById("password");

    // Elementi per gli errori (div alert-error)
    const errEmailDiv = document.getElementById("err-email");
    const errEmailText = document.getElementById("err-email-text");
    const errPassDiv = document.getElementById("err-password");
    const errPassText = document.getElementById("err-password-text");

    // Regex per formato Email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    // --- Validazione Form ---
    if (teamForm) {
        teamForm.addEventListener("submit", function(event) {

            // Se stiamo eliminando, bypassa la validazione campi
            if (inputAzione.value === "elimina") {
                return;
            }

            let isValid = true;

            const emailVal = emailInput.value.trim();
            const passVal = passInput.value.trim();
            const isNewAdmin = document.getElementById("isNewAdmin").value === "true";

            // Reset stili errori prima di validare
            emailInput.style.borderColor = "#333";
            errEmailDiv.style.display = "none";
            passInput.style.borderColor = "#333";
            errPassDiv.style.display = "none";

            // Validazione Email (sempre obbligatoria)
            if (emailVal === "") {
                emailInput.style.borderColor = "#e74c3c";
                errEmailText.textContent = "L'indirizzo email è obbligatorio.";
                errEmailDiv.style.display = "block";
                isValid = false;
            } else if (!emailRegex.test(emailVal)) {
                emailInput.style.borderColor = "#e74c3c";
                errEmailText.textContent = "Formato email non valido.";
                errEmailDiv.style.display = "block";
                isValid = false;
            }

            // Validazione Password (obbligatoria solo per nuovo admin)
            if (isNewAdmin && passVal === "") {
                passInput.style.borderColor = "#e74c3c";
                errPassText.textContent = "La password è obbligatoria per un nuovo amministratore.";
                errPassDiv.style.display = "block";
                isValid = false;
            } else if (passVal !== "" && passVal.length < 6) {
                passInput.style.borderColor = "#e74c3c";
                errPassText.textContent = "La password deve contenere almeno 6 caratteri.";
                errPassDiv.style.display = "block";
                isValid = false;
            }

            // Se la validazione fallisce, blocco il submit
            if (!isValid) {
                event.preventDefault();
            }
        });
    }

    // --- Reset errori quando l'utente inizia a digitare (come in dettaglio attributo) ---
    if (emailInput) {
        emailInput.addEventListener("input", function() {
            this.style.borderColor = "#333";
            if (errEmailDiv) errEmailDiv.style.display = "none";
        });
    }

    if (passInput) {
        passInput.addEventListener("input", function() {
            this.style.borderColor = "#333";
            if (errPassDiv) errPassDiv.style.display = "none";
        });
    }

    // --- Gestione Eliminazione ---
    if (btnElimina) {
        btnElimina.addEventListener("click", function() {
            if (confirm("ATTENZIONE: Sei sicuro di voler revocare l'accesso a questo amministratore? L'operazione è irreversibile.")) {
                inputAzione.value = "elimina";
                teamForm.submit();
            }
        });
    }

});