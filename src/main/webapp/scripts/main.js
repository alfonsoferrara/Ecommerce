document.addEventListener("DOMContentLoaded", function() {
    console.log("Script main.js caricato correttamente!");

    const hamburger = document.querySelector(".hamburger");
    const navMenu = document.querySelector(".nav-menu");

    if (hamburger && navMenu) {
        hamburger.addEventListener("click", function() {
            navMenu.classList.toggle("active");
            console.log("Click rilevato! Classe 'active' attivata/disattivata.");
        });
    } else {
        console.error("ERRORE: Impossibile trovare la classe .hamburger o .nav-menu nel DOM.");
    }
});