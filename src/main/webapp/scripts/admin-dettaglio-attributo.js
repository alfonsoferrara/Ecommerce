document.addEventListener("DOMContentLoaded", function() {

    const attrForm = document.getElementById("attributoForm");
    const nameInput = document.getElementById("nome");

    if (attrForm) {
        attrForm.addEventListener("submit", function(event) {
            const nomeValue = nameInput.value.trim();

            if (nomeValue === "") {
                event.preventDefault(); // Blocca l'invio
                document.getElementById("nomeVuoto").style.display = "block";
                nameInput.focus();
                nameInput.style.borderColor = "#e74c3c";
            }
        });

        // Reset del bordo rosso quando l'utente ricomincia a scrivere
        nameInput.addEventListener("input", function() {
            document.getElementById("nomeVuoto").style.display = "none";
            this.style.borderColor = "#333";
        });
    }

    // --- Gestione Eliminazione ---
    if (btnElimina) {
        btnElimina.addEventListener("click", function() {
            if (confirm("Sei sicuro di voler eliminare questo attributo? Se è associato a dei prodotti, l'operazione potrebbe essere bloccata.")) {
                inputAzione.value = "elimina";
                attrForm.submit();
            }
        });
    }

});