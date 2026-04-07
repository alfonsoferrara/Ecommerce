document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("adminLoginForm");
    const errorMessage = document.getElementById("error-message");

    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            // Recupera i valori eliminando gli spazi bianchi laterali
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();
            
            // Variabile per tracciare se ci sono errori
            let hasError = false;

            if (email === "" || password === "") {
                hasError = true;
            }

            if (hasError) {
                // Blocca l'invio del form
                event.preventDefault();
                // Mostra il div di errore
                errorMessage.textContent = "Errore: Inserisci email e password per accedere.";
                errorMessage.style.display = "block";
                
                // Nasconde eventuali errori del server per non creare confusione visiva
                const serverError = document.querySelector('.server-error');
                if(serverError) {
                    serverError.style.display = 'none';
                }
            } else {
                // Se tutto è ok, nascondo l'errore (nel caso fosse rimasto visibile)
                errorMessage.style.display = "none";
            }
        });
    }
});