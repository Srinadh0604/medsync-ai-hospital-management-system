// const API_URL = 
// "https://medsync-ai-hospital-management-system.onrender.com";

async function loadPatientsDropdown() {

    try {

        const response = await fetch(
            `${API_URL}/patients?page=0&size=100`,
            {
                headers: {
                    Authorization:
                        `Bearer ${localStorage.getItem("token")}`
                }
            }
        );

        if (!response.ok) {
            throw new Error(
                `HTTP Error ${response.status}`
            );
        }

        const data = await response.json();
        // window.open(data.url, "_blank");

        const dropdown =
            document.getElementById("patientId");

        dropdown.innerHTML =
            '<option value="">Select Patient</option>';

        data.content.forEach(patient => {

            dropdown.innerHTML += `
                <option value="${patient.id}">
                   
                    ${patient.id} - ${patient.name}
                </option>
            `;
        });

    } catch (error) {

        console.error(error);
    }
}

async function generateReport() {

    const patientId =
        document.getElementById(
            "patientId"
        ).value;

    const response =
        await fetch(
            `${API_URL}/pdf/url/${patientId}`
        );

    const pdfUrl =
        await response.text();

    window.open(pdfUrl, "_blank");
}

async function sendReport() {

    const patientId =
        document.getElementById("patientId").value;

    try {

        console.log(localStorage.getItem("token"));
        console.log(authHeaders());
        const response =
            await fetch(
                `${API_URL}/reports/send/${patientId}`,
                {
                    method: "POST",
                    headers: authHeaders()
                }
            );

        console.log("Status:", response.status);
        const data =
            await response.json();

        alert(data.message);

        console.log(data.s3Url);

        // alert(message);
        alert(
            message || "Report sent successfully"
        );

    } catch (error) {

        console.error(error);
    }
}

loadPatientsDropdown();