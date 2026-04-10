document.addEventListener("DOMContentLoaded", function() {
    const clienteForm = document.getElementById("clienteForm");

    if (clienteForm) {
        // Mappatura dei campi standard da validare
        const inputsToValidate = [
            { element: document.getElementById("nome"), error: document.getElementById("err-nome"), msg: "Il nome non può essere vuoto." },
            { element: document.getElementById("cognome"), error: document.getElementById("err-cognome"), msg: "Il cognome non può essere vuoto." },
            { element: document.getElementById("email"), error: document.getElementById("err-email"), msg: "L'email non può essere vuota." }
        ];

        const passwordInput = document.getElementById("password");
        const errPassword = document.getElementById("err-password");
        const isNewCustomer = document.getElementById("isNewCustomer").value === "true";

        clienteForm.addEventListener("submit", function(event) {
            // Se il tasto premuto è "Elimina", salta la validazione
            if (event.submitter && event.submitter.value === "elimina") {
                return;
            }

            let hasError = false;

            // Controlla i campi di testo
            inputsToValidate.forEach(inputObj => {
                if (inputObj.element.value.trim() === "") {
                    hasError = true;
                    inputObj.error.innerHTML = `<i class="fas fa-exclamation-triangle"></i> ${inputObj.msg}`;
                    inputObj.error.style.display = "block";
                    inputObj.element.style.borderColor = "#e74c3c";
                }
            });

            // Controlla la logica specifica per la password
            const pwdVal = passwordInput.value.trim();
            if (isNewCustomer && pwdVal.length < 6) {
                hasError = true;
                errPassword.innerHTML = `<i class="fas fa-exclamation-triangle"></i> Inserisci una password di almeno 6 caratteri.`;
                errPassword.style.display = "block";
                passwordInput.style.borderColor = "#e74c3c";
            } else if (!isNewCustomer && pwdVal.length > 0 && pwdVal.length < 6) {
                // Se non è nuovo ma sta provando a cambiare password con una troppo corta
                hasError = true;
                errPassword.innerHTML = `<i class="fas fa-exclamation-triangle"></i> La nuova password deve avere almeno 6 caratteri.`;
                errPassword.style.display = "block";
                passwordInput.style.borderColor = "#e74c3c";
            }

            // Se ci sono errori, blocca l'invio del form
            if (hasError) {
                event.preventDefault();
            }
        });

        // Reset del bordo rosso e nasconde l'errore quando l'utente ricomincia a scrivere
        inputsToValidate.forEach(inputObj => {
            inputObj.element.addEventListener("input", function() {
                inputObj.error.style.display = "none";
                this.style.borderColor = "#333";
            });
        });

        // Reset per l'input password
        if (passwordInput) {
            passwordInput.addEventListener("input", function() {
                errPassword.style.display = "none";
                this.style.borderColor = "#333";
            });
        }
    }
});