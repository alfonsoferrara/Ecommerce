document.addEventListener("DOMContentLoaded", function() {
    const categoryForm = document.getElementById("categoryForm");
    const nameInput = document.getElementById("nome");

    if (categoryForm) {
        categoryForm.addEventListener("submit", function(event) {
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
});