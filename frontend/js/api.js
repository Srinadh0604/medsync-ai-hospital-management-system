const BASE_URL =
"http://localhost:8080";


function authHeaders(){

    return {
        "Authorization":
        "Bearer " +
        localStorage.getItem("token"),

        "Content-Type":
        "application/json"
    };
}