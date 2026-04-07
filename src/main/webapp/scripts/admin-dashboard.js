document.addEventListener("DOMContentLoaded", function() {
    // Evidenzia la voce del menu corrente
    const currentLocation = location.pathname;
    const menuItems = document.querySelectorAll(".sidebar-menu li a");

    menuItems.forEach(item => {
        const itemHref = item.getAttribute("href");
        // Se l'URL della pagina include l'href del link, aggiungiamo la classe active
        if (currentLocation.includes(itemHref) && itemHref !== "#") {
            item.classList.add("active");
        }
    });
});