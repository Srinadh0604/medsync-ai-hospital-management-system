const API_URL =
"https://medsync-ai-hospital-management-system.onrender.com";

// "http://localhost:8080";
function authHeaders() {
    return {
        Authorization:
            `Bearer ${localStorage.getItem("token")}`,
        "Content-Type":
            "application/json"
    };
}

function logout() {

    localStorage.removeItem("token");

    window.location.href = "index.html";
}