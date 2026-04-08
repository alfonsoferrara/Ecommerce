document.addEventListener("DOMContentLoaded", function() {
    const updateForm = document.getElementById("statusUpdateForm");
    
    if (updateForm) {
        updateForm.addEventListener("submit", function(event) {
            const statusSelect = document.getElementById("statoCambiato");
            const newStatus = statusSelect.options[statusSelect.selectedIndex].text;
            
            const confirmed = confirm(`Sei sicuro di voler cambiare lo stato dell'ordine in "${newStatus}"?`);
            
            if (!confirmed) {
                event.preventDefault(); // Blocca l'invio se l'utente clicca su "Annulla"
            }
        });
    }
});