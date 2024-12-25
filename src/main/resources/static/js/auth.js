document.addEventListener("DOMContentLoaded", () => {
    const loginModal = document.getElementById("login-modal");
    const registerModal = document.getElementById("register-modal");
    const closeLoginModal = document.getElementById("close-login-modal");
    const closeRegisterModal = document.getElementById("close-register-modal");
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");
    const loginError = document.getElementById("login-error");
    const registerError = document.getElementById("register-error");
    const registerButton = document.getElementById("register-button");
    const passwordInput = document.getElementById("password");

    closeLoginModal.addEventListener("click", () => {
        loginModal.style.display = "none";
        passwordInput.value = "";
    });

    registerButton.addEventListener("click", () => {
        loginModal.style.display = "none";
        registerModal.style.display = "flex";
        registerError.style.display = "none";
    });

    closeRegisterModal.addEventListener("click", () => {
        registerModal.style.display = "none";
    });

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = passwordInput.value;

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                loginModal.style.display = "none";
                location.reload();
            } else {
                loginError.style.display = "block";
            }
        } catch (error) {
            console.error("Error during login:", error);
            loginError.style.display = "block";
        }
    });

    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = {
            firstname: document.getElementById("firstname").value,
            lastname: document.getElementById("lastname").value,
            phone: document.getElementById("phone").value,
            email: document.getElementById("email").value,
            password: document.getElementById("reg-password").value,
        };

        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                registerModal.style.display = "none";
                loginModal.style.display = "flex";
                loginError.style.display = "none";
            } else if (response.status === 400) {
                const errors = await response.json();
                registerError.textContent = errors.map(err => err.defaultMessage).join(", ");
                registerError.style.display = "block";
            } else if (response.status === 409) {
                registerError.textContent = "Email is already in use.";
                registerError.style.display = "block";
            } else {
                registerError.textContent = "An unexpected error occurred.";
                registerError.style.display = "block";
            }
        } catch (error) {
            console.error("Error during registration:", error);
            registerError.textContent = "An unexpected error occurred.";
            registerError.style.display = "block";
        }
    });

    window.addEventListener("click", (event) => {
        if (event.target === loginModal) {
            loginModal.style.display = "none";
            passwordInput.value = "";
        } else if (event.target === registerModal) {
            registerModal.style.display = "none";
            document.getElementById("reg-password").value = "";
        }
    });
});
