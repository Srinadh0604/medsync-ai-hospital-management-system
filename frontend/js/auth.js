async function login() {

    const email =
        document.getElementById("email").value;

    const password =
        document.getElementById("password").value;

    const response =
        await fetch(
            `${API_URL}/auth/login`,
            {
                method:"POST",

                headers:{
                    "Content-Type":
                    "application/json"
                },

                body:JSON.stringify({
                    email,
                    password
                })
            }
        );

    if(!response.ok){

        alert("Invalid Credentials");

        return;
    }

    // const token =
    //     await response.text();

    // localStorage.setItem(
    //     "token",
    //     token
    // );

const data =
    await response.json();

localStorage.setItem(
    "token",
    data.token
);

localStorage.setItem(
    "role",
    data.role
);

window.location.href =
    "dashboard.html";


}