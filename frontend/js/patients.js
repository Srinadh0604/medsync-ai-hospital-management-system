//  const API_URL = "https://medsync-ai-hospital-management-system.onrender.com";


// if (
//     getRole() !== "ADMIN" &&
//     getRole() !== "RECEPTIONIST"
// ) {

//     alert("Access Denied");
//     window.location.href =
//         "dashboard.html";

// } else {

//     loadPatients();
// }
async function loadPatients() {

    try {

        const response =
            // await fetch(`${API_URL}/patients`);
            await fetch(
                `${API_URL}/patients`,
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

        const data =
            await response.json();

        const patients =
            data.content;

        const table =
            document.getElementById("patientTable");

        table.innerHTML = "";

        patients.forEach(patient => {

            table.innerHTML += `
                <tr>
                    <td>${patient.id}</td>
                    <td>${patient.name}</td>
                    <td>${patient.age}</td>
                    <td>${patient.disease}</td>

                     <td>
        ${patient.doctor
                    ? patient.doctor.name
                    : "Not Assigned"}
    </td>

                    <td>
                        <button onclick="deletePatient(${patient.id})">
                            Delete
                        </button>
                    </td>
                </tr>
            `;
        });

    } catch (error) {

        console.error(error);
    }
}

async function addPatient() {

    const button =
        document.querySelector("button");

    button.disabled = true;

    const patient = {
        name: document.getElementById("name").value,
        age: parseInt(document.getElementById("age").value),
        disease: document.getElementById("disease").value,
        prescription: document.getElementById("prescription").value,
        email: document.getElementById("email").value,
        doctorId: parseInt(document.getElementById("doctorId").value)
    };

    try {

        const response = await fetch(
            `${API_URL}/patients`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization:
                        `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify(patient)
            }
        );

        if (response.ok) {

            alert("Patient Added Successfully");

            loadPatients();

            document.getElementById("name").value = "";
            document.getElementById("age").value = "";
            document.getElementById("disease").value = "";
            document.getElementById("prescription").value = "";
            document.getElementById("email").value = "";
            document.getElementById("doctorId").value = "";
        }

    } finally {

        button.disabled = false;
    }
}




async function deletePatient(id) {

  await fetch(
    `${API_URL}/patients/${id}`,
    {
        method: "DELETE",
        headers: {
            Authorization:
                `Bearer ${localStorage.getItem("token")}`
        }
    }
);

    loadPatients();
}
loadPatients();
