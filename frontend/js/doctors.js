const userRole =
    localStorage.getItem("role");

if (userRole !== "ADMIN") {

    alert("Access Denied");
    window.location.href =
        "dashboard.html";
}

async function loadDoctors() {

    const response =
        // await fetch(
        //     "http://localhost:8080/doctors"
        // );

        await fetch(
            `${API_URL}/doctors`,
            {
                headers: authHeaders()
            }
        )


    if (!response.ok) {

        console.error(
            "Failed to load doctors"
        );

        return;
    }

    const doctors =
        await response.json();

    const tbody =
        document.querySelector(
            "#doctorTable tbody"
        );

    tbody.innerHTML = "";

    doctors.forEach(doctor => {

        tbody.innerHTML += `
            <tr>
                <td>${doctor.id}</td>
                <td>${doctor.name}</td>
                <td>${doctor.specialization}</td>
            </tr>
        `;
    });
}

loadDoctors();


async function addDoctor() {

    const name =
        document.getElementById("name").value;

    const specialization =
        document.getElementById(
            "specialization"
        ).value;

    const response =
        await fetch(
            `${API_URL}/doctors`,
            {
                method: "POST",

                // headers: {
                //     "Content-Type":
                //         "application/json"
                // },
                 headers: {
            ...authHeaders()
        },

                body: JSON.stringify({

                    name,
                    specialization

                })
            }
        );

    if (response.ok) {

        alert(
            "Doctor Added Successfully"
        );

        loadDoctors();

    } else {

        alert(
            "Failed to Add Doctor"
        );
    }
}