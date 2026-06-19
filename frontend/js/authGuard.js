
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "index.html";
}

// auth-guard.js

const role =
    localStorage.getItem("role");

function requireRoles(...allowed){

    if(!allowed.includes(role)){

        alert("Access Denied");

        window.location.href =
            "dashboard.html";
    }
}