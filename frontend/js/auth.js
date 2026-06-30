const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const rememberMeInput = document.getElementById("rememberMe");
const loginButton = document.getElementById("loginButton");
const registerButton = document.getElementById("registerButton");
const formStatus = document.getElementById("formStatus");
const themeToggle = document.getElementById("themeToggle");

function setFormStatus(message, isError = false) {
    if (formStatus) {
        formStatus.textContent = message;
        formStatus.classList.toggle("error", isError);
    }
}

function showFieldError(inputId, message) {
    const errorElement = document.getElementById(`${inputId}Error`);
    if (errorElement) {
        errorElement.textContent = message;
    }
}

function clearFieldErrors(fieldIds) {
    fieldIds.forEach((fieldId) => showFieldError(fieldId, ""));
}

function validateLoginForm() {
    clearFieldErrors(["email", "password"]);
    const email = emailInput.value.trim();
    const password = passwordInput.value;
    let isValid = true;

    if (!email) {
        showFieldError("email", "Email is required.");
        isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        showFieldError("email", "Please enter a valid email address.");
        isValid = false;
    }

    if (!password) {
        showFieldError("password", "Password is required.");
        isValid = false;
    } else if (password.length < 6) {
        showFieldError("password", "Password must be at least 6 characters.");
        isValid = false;
    }

    return isValid;
}

function validateRegisterForm() {
    clearFieldErrors(["registerName", "registerEmail", "registerPassword", "confirmPassword"]);
    const name = document.getElementById("registerName").value.trim();
    const email = document.getElementById("registerEmail").value.trim();
    const password = document.getElementById("registerPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    let isValid = true;

    if (!name) {
        showFieldError("registerName", "Name is required.");
        isValid = false;
    } else if (name.length < 2) {
        showFieldError("registerName", "Name must be at least 2 characters.");
        isValid = false;
    }

    if (!email) {
        showFieldError("registerEmail", "Email is required.");
        isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        showFieldError("registerEmail", "Please enter a valid email address.");
        isValid = false;
    }

    if (!password) {
        showFieldError("registerPassword", "Password is required.");
        isValid = false;
    } else if (password.length < 6) {
        showFieldError("registerPassword", "Password must be at least 6 characters.");
        isValid = false;
    }

    if (!confirmPassword) {
        showFieldError("confirmPassword", "Please confirm your password.");
        isValid = false;
    } else if (password !== confirmPassword) {
        showFieldError("confirmPassword", "Passwords do not match.");
        isValid = false;
    }

    return isValid;
}

function setLoadingState(button, isLoading) {
    if (!button) {
        return;
    }

    button.classList.toggle("is-loading", isLoading);
    button.disabled = isLoading;
}

function applySavedTheme() {
    const storedTheme = localStorage.getItem("medsync-theme");
    if (storedTheme === "dark") {
        document.body.classList.add("theme-dark");
        if (themeToggle) {
            themeToggle.querySelector(".theme-icon").textContent = "☀️";
        }
    }
}

function toggleTheme() {
    const isDark = document.body.classList.toggle("theme-dark");
    localStorage.setItem("medsync-theme", isDark ? "dark" : "light");
    if (themeToggle) {
        themeToggle.querySelector(".theme-icon").textContent = isDark ? "☀️" : "🌙";
    }
}

async function login() {
    if (!validateLoginForm()) {
        setFormStatus("Please fix the highlighted fields.", true);
        return;
    }

    setLoadingState(loginButton, true);
    setFormStatus("Signing you in...");

    const email = emailInput.value.trim();
    const password = passwordInput.value;

    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            throw new Error("Invalid credentials");
        }

        const data = await response.json();
        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);
        localStorage.setItem("name", data.name);

        if (rememberMeInput && rememberMeInput.checked) {
            localStorage.setItem("rememberedEmail", email);
        } else {
            localStorage.removeItem("rememberedEmail");
        }

        window.location.href = "dashboard.html";
    } catch (error) {
        setFormStatus("Invalid email or password. Please try again.", true);
    } finally {
        setLoadingState(loginButton, false);
    }
}

async function register() {
    if (!validateRegisterForm()) {
        setFormStatus("Please fix the highlighted fields.", true);
        return;
    }

    setLoadingState(registerButton, true);
    setFormStatus("Creating your account...");

    const name = document.getElementById("registerName").value.trim();
    const email = document.getElementById("registerEmail").value.trim();
    const password = document.getElementById("registerPassword").value;

    try {
        const response = await fetch(`${API_URL}/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ name, email, password, role: "PATIENT" })
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || "Unable to create account.");
        }

        setFormStatus("Account created successfully. Redirecting to sign in...", false);
        setTimeout(() => {
            window.location.href = "index.html";
        }, 800);
    } catch (error) {
        setFormStatus(error.message || "Unable to create account. Please try again.", true);
    } finally {
        setLoadingState(registerButton, false);
    }
}

if (loginForm) {
    loginForm.addEventListener("submit", function (event) {
        event.preventDefault();
        login();
    });
}

if (registerForm) {
    registerForm.addEventListener("submit", function (event) {
        event.preventDefault();
        register();
    });
}

if (themeToggle) {
    themeToggle.addEventListener("click", toggleTheme);
}

if (emailInput && passwordInput) {
    [emailInput, passwordInput].forEach((input) => {
        input.addEventListener("input", () => {
            if (formStatus && formStatus.textContent) {
                setFormStatus("");
            }
        });
    });
}

const rememberedEmail = localStorage.getItem("rememberedEmail");
if (rememberedEmail && emailInput) {
    emailInput.value = rememberedEmail;
}

if (rememberMeInput && rememberedEmail) {
    rememberMeInput.checked = true;
}

applySavedTheme();