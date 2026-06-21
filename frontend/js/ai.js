// const API_URL =  "http://localhost:8080";

// const API_URL = "https://medsync-ai-hospital-management-system.onrender.com";

// import { API_URL } from "./config/api";
Authorization:
`Bearer ${localStorage.getItem("token")}`
const userRole =
    localStorage.getItem("role");

if (
    userRole !== "ADMIN" &&
    userRole !== "DOCTOR"
) {
    window.location.href =
        "dashboard.html";
}

async function getSummary() {

    const patientId =
        document.getElementById("patientId").value;

    try {

        const response =
            //         await fetch(
            //               `http://localhost:8080/ai/patient-summary/${patientId}`,
            //     {
            //         headers: {
            //             Authorization:
            //                 `Bearer ${token}`
            //         }
            //     }
            // );
            await fetch(
                `${API_URL}/ai/patient-summary/${patientId}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

        if (!response.ok) {
            throw new Error(
                `HTTP Error ${response.status}`
            );
        }

        const summary =
            await response.text();

        document.getElementById("summaryBox")
            .innerHTML =
            `<p>${summary}</p>`;

    } catch (error) {

        console.error(error);
    }
}

async function getSuggestion() {

    const disease =
        document.getElementById("disease").value;

    if (!disease) {

        alert("Enter disease");

        return;
    }

    try {

        const response =
            await fetch(
                `${API_URL}/ai/suggestion?disease=${encodeURIComponent(disease)}`
            );

        if (!response.ok) {

            throw new Error(
                `HTTP ${response.status}`
            );
        }

        const result =
            await response.text();

        document.getElementById("summaryBox")
            .innerHTML = result;

    } catch (error) {

        console.error(error);
    }
}

async function getSuggestionForPatient() {

    const patientId =
        document.getElementById("patientId").value;

    // Get patient details
    const patientResponse =
        await fetch(
            `${API_URL}/patients/${patientId}`
        );

    const patient =
        await patientResponse.json();

    const disease =
        patient.disease;

    // Get AI suggestion
    const aiResponse =
        // await fetch(
        //     `${API_URL}/ai/suggestion?disease=${encodeURIComponent(disease)}`
        // );
        //   await  fetch(
        //         `http://localhost:8080/ai/suggestion?disease=${disease}`,
        //         {
        //             headers: authHeaders()
        //         }
        //     )
        await fetch(
            `${API_URL}/ai/suggestion?disease=${encodeURIComponent(disease)}`,
            {
                headers: authHeaders()
            }
        );

    const suggestion =
        await aiResponse.text();

    document.getElementById("summaryBox")
        .innerHTML = suggestion;
}
async function loadPatientsDropdown() {

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

    const dropdown =
        document.getElementById("patientId");

    data.content.forEach(patient => {

        dropdown.innerHTML += `
            <option value="${patient.id}">
                ${patient.name}
            </option>
        `;
    });
}

loadPatientsDropdown();

