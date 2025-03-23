document.getElementById("loginButton").addEventListener("click", function (event) {
    event.preventDefault();

    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    if (email === "" || password === "") {
        alert("Please enter both email and password.");
        return;
    }

    // Create the user object to send to the backend
    let user = {
        email: email,
        password: password
    };

    // Send the user data to the backend for authentication
    fetch("http://localhost:8080/api/v1/auth/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 201) {
                // Store the token in localStorage or sessionStorage
                localStorage.setItem("authToken", data.data.token);
                // Redirect to the dashboard
                window.location.href = "dashboard.html";  // Modify this path accordingly
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error("Error during login:", error);
            alert("Login failed. Please try again.");
        });
});
