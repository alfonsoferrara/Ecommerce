document.addEventListener('DOMContentLoaded', function() {
    
    // --- Toggle Password (uguale al login) ---
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function () {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            this.classList.toggle('fa-eye');
            this.classList.toggle('fa-eye-slash');
        });
    }

    // --- Validazione Form Registrazione ---
    const form = document.getElementById('registerForm');
    const nome = document.getElementById('nome');
    const cognome = document.getElementById('cognome');
    const email = document.getElementById('email');
    const telefono = document.getElementById('telefono');

    // Funzioni di supporto DOM
    function showError(input, message) {
        input.classList.add('input-error');
        const errorDiv = input.parentElement.querySelector('.error-message');
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }

    function clearError(input) {
        input.classList.remove('input-error');
        const errorDiv = input.parentElement.querySelector('.error-message');
        errorDiv.style.display = 'none';
    }

    // --- Funzioni di validazione specifiche ---
    
    function validateNome() {
        // Regex: Solo lettere (anche accentate) e spazi, lunghezza minima 2
        const nameRegex = /^[a-zA-Zàèéìòù\'\s]{2,50}$/;
        if (!nameRegex.test(nome.value.trim())) {
            showError(nome, "Inserisci un nome valido (min 2 lettere).");
            return false;
        }
        clearError(nome);
        return true;
    }

    function validateCognome() {
        const nameRegex = /^[a-zA-Zàèéìòù\'\s]{2,50}$/;
        if (!nameRegex.test(cognome.value.trim())) {
            showError(cognome, "Inserisci un cognome valido (min 2 lettere).");
            return false;
        }
        clearError(cognome);
        return true;
    }

    function validateEmail() {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email.value.trim())) {
            showError(email, "Formato email non valido.");
            return false;
        }
        clearError(email);
        return true;
    }

    function validateTelefono() {
        // Regex: Solo numeri, tra 9 e 10 cifre
        const phoneRegex = /^[0-9]{9,10}$/;
        if (!phoneRegex.test(telefono.value.trim())) {
            showError(telefono, "Inserisci un numero valido (9 o 10 cifre).");
            return false;
        }
        clearError(telefono);
        return true;
    }

    function validatePassword() {
        // Didattico: almeno 6 caratteri e almeno un numero
        const passRegex = /^(?=.*[0-9]).{6,}$/;
        if (!passRegex.test(passwordInput.value)) {
            showError(passwordInput, "Almeno 6 caratteri e 1 numero.");
            return false;
        }
        clearError(passwordInput);
        return true;
    }

    // Event listeners al cambio campo (UX dinamica)
    nome.addEventListener('change', validateNome);
    cognome.addEventListener('change', validateCognome);
    email.addEventListener('change', validateEmail);
    telefono.addEventListener('change', validateTelefono);
    passwordInput.addEventListener('change', validatePassword);

    // Event listener al click su Registrati
    form.addEventListener('submit', function(e) {
        // Eseguo tutte le validazioni
        const isNomeValid = validateNome();
        const isCognomeValid = validateCognome();
        const isEmailValid = validateEmail();
        const isPhoneValid = validateTelefono();
        const isPassValid = validatePassword();

        // Se anche solo una validazione è "false", blocco l'invio
        if (!isNomeValid || !isCognomeValid || !isEmailValid || !isPhoneValid || !isPassValid) {
            e.preventDefault();
        }
    });
});