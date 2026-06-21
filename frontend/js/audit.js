// const API_URL = "https://medsync-ai-hospital-management-system.onrender.com";
//  "http://localhost:8080";

const userRole =
    localStorage.getItem("role");

if(userRole !== "ADMIN"){

    alert("Access Denied");

    window.location.href =
        "dashboard.html";
}
async function loadAuditLogs() {

    try {

        const response =
            // await fetch(`${API_URL}/audit/logs`);
          await   fetch(
                `${API_URL}/audit/logs`,
                {
                    headers: authHeaders()
                }
            )

            console.log("Status:", response.status);
console.log("OK:", response.ok);
    
        if (!response.ok) {
            throw new Error(
                `HTTP Error ${response.status}`
            );
        }

        const logs =
            await response.json();

        const table =
            document.getElementById("auditTable");

        table.innerHTML = "";

        logs.forEach(log => {

            table.innerHTML += `
                <tr>
                    <td>${log.id}</td>
                    <td>${log.username}</td>
                    <td>${log.action}</td>
                    <td>${log.timestamp}</td>
                </tr>
            `;
        });

    } catch (error) {

        console.error(error);
    }
}

loadAuditLogs();