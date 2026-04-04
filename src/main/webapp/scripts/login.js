document.addEventListener('DOMContentLoaded', function() {
    // --- Toggle Password ---
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

    // --- Validazione Form ---
    const form = document.getElementById('loginForm');
    const email = document.getElementById('email');

    // Funzioni di supporto per mostrare/nascondere errori nel DOM
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

    // Regole di validazione (Regex)
    function validateEmail() {
        // Regex: stringa@stringa.stringa
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email.value.trim()) {
            showError(email, "L'email è obbligatoria.");
            return false;
        } else if (!emailRegex.test(email.value.trim())) {
            showError(email, "Formato email non valido.");
            return false;
        }
        clearError(email);
        return true;
    }

    function validatePassword() {
        if (!passwordInput.value.trim()) {
            showError(passwordInput, "La password è obbligatoria.");
            return false;
        }
        clearError(passwordInput);
        return true;
    }

    // Validazione al rilascio o cambio ('input' scatta mentre digiti, 'change' quando clicchi fuori)
    email.addEventListener('change', validateEmail);
    passwordInput.addEventListener('change', validatePassword);

    // Validazione finale al Submit
    form.addEventListener('submit', function(e) {
        const isEmailValid = validateEmail();
        const isPasswordValid = validatePassword();

        // Se anche solo uno è falso, blocco l'invio al server
        if (!isEmailValid || !isPasswordValid) {
            e.preventDefault(); 
        }
    });
});