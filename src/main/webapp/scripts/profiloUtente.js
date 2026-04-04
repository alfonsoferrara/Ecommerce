document.addEventListener('DOMContentLoaded', function() {

    // 1. Saluto dinamico in base all'orario
    const greetingElement = document.getElementById('dynamic-greeting');
    if (greetingElement) {
        const hour = new Date().getHours();
        let greeting = 'Ciao';

        if (hour >= 5 && hour < 13) {
            greeting = 'Buongiorno';
        } else if (hour >= 13 && hour < 18) {
            greeting = 'Buon pomeriggio';
        } else {
            greeting = 'Buonasera';
        }

        greetingElement.textContent = greeting;
    }

    // 2. Conferma prima del Logout per evitare click accidentali
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', function(e) {
            const confirmed = confirm('Sei sicuro di voler uscire dal tuo account?');
            if (!confirmed) {
                e.preventDefault(); // Blocca il reindirizzamento se l'utente annulla
            }
        });
    }

});