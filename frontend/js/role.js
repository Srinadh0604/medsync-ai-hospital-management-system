function hideMenu(id) {

     const element =
        document.getElementById(id);

    if (element) {

        element.style.display =
            "none";
    }
}

function applyRolePermissions() {

    const role =
        getRole();

    if (role === "ADMIN") {

        return;
    }

    if (role === "DOCTOR") {

        hideMenu("doctorMenu");

        hideMenu("auditMenu");
    }

    if (role === "RECEPTIONIST") {

        hideMenu("doctorMenu");

        hideMenu("auditMenu");

        hideMenu("aiMenu");
    }
}
function getRole() {
    return localStorage.getItem("role");
}