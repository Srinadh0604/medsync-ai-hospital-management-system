const API_URL = "https://medsync-ai-hospital-management-system.onrender.com";
// "http://localhost:8080";

async function loadAppointments() {

    try {

        // const response =
        //     await fetch(`${API_URL}/appointments`);
        const response = await fetch(
            `${API_URL}/appointments`,
            {
                headers: {
                    Authorization:
                        `Bearer ${localStorage.getItem("token")}`
                }
            }
        );

        const appointments =
            await response.json();

        const table =
            document.getElementById("appointmentTable");

        table.innerHTML = "";

        appointments.forEach(a => {

            table.innerHTML += `
                <tr>

                    <td>${a.id}</td>

                    <td>${a.patient.id}</td>

                    <td>${a.patient.name}</td>

                    <td>${a.doctor.id}</td>

                    <td>${a.doctor.name}</td>

                    <td>${a.appointmentTime}</td>

                    <td>${a.status}</td>

                    <td>
                        <button
                            onclick="deleteAppointment(${a.id})"
                            class="delete-btn">

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

async function addAppointment() {

    const appointment = {

        patientId:
            parseInt(
                document.getElementById("patientId").value
            ),

        doctorId:
            parseInt(
                document.getElementById("doctorId").value
            ),

        appointmentTime:
            document.getElementById(
                "appointmentTime"
            ).value,

        status:
            document.getElementById("status").value
    };

    try {

        await fetch(
            `${API_URL}/appointments`,
            {
                method: "POST",
                headers: authHeaders(),
                body: JSON.stringify(appointment)
            }
        );

        // await fetch(
        //     `${API_URL}/appointments`,
        //     {
        //         method: "POST",

        //         headers: {
        //             "Content-Type":
        //                 "application/json"
        //         },

        //         body:
        //             JSON.stringify(appointment)
        //     }
        // );

        document.getElementById(
            "patientId"
        ).value = "";

        document.getElementById(
            "doctorId"
        ).value = "";

        document.getElementById(
            "appointmentTime"
        ).value = "";

        document.getElementById(
            "status"
        ).selectedIndex = 0;

        loadAppointments();

    } catch (error) {

        console.error(error);
    }
    const status =
        document.getElementById("status").value;

    if (!status) {
        // alert("Please select a status");
        return;
    }
}

loadAppointments();

async function deleteAppointment(id) {

    try {

        await fetch(
            `${API_URL}/appointments/${id}`,
            {
                method: "DELETE",
                 headers: authHeaders()
            }
        );

        loadAppointments();

    } catch (error) {

        console.error(error);
    }
}