

// function logout() {

//     if (confirm("Are you sure you want to logout?")) {

//         localStorage.removeItem("token");
//         window.location.href = "index.html";
//     }
// }

function logout() {

    if (confirm("Are you sure you want to logout?")) {

        localStorage.removeItem("token");
        localStorage.removeItem("role");
        localStorage.removeItem("name");
        localStorage.removeItem("rememberedEmail"); // Optional

        window.location.href = "index.html";
    }
}

function renderChart(data) {

    new Chart(
        document.getElementById("statsChart"),
        {
            type: "pie",

            // type:"bar",

            data: {
                labels: [
                    "Doctors",
                    "Patients",
                    "Users",
                    "Reports",
                    "Audit Logs"
                ],

                datasets: [{
                    label: "Hospital Statistics",

                    data: [
                        data.totalDoctors,
                        data.totalPatients,
                        data.totalUsers,
                        data.totalReportsGenerated,
                        data.totalAuditLogs
                    ]
                }]
            }
        }
    );
}

const name = localStorage.getItem("name");

document.getElementById("welcomeText").innerHTML =
    `Welcome ${name} 👋`;

async function loadDashboard() {

    const response =
        await fetch(
        `${API_URL}/dashboard/stats`,
        {
            headers: authHeaders()
        }
    );

    const data =
        await response.json();

    document.getElementById("doctorCount").innerText =
        data.totalDoctors;

    document.getElementById("patientCount").innerText =
        data.totalPatients;

    document.getElementById("userCount").innerText =
        data.totalUsers;

    document.getElementById("reportCount").innerText =
        data.totalReportsGenerated;

    document.getElementById("auditCount").innerText =
        data.totalAuditLogs;

    renderChart(data);
}


loadDashboard();