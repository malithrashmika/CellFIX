$(document).ready(function () {
    $("#registerForm").submit(function (event) {
        event.preventDefault();

        var userData = {
            f_name: $("#inputFirstName").val(),
            l_name: $("#inputLastName").val(),
            email: $("#inputEmail").val(),
            password: $("#inputPassword").val(),
            c_password: $("#inputPasswordConfirm").val()
        };

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/v1/user/register",  // Backend API
            contentType: "application/json",
            data: JSON.stringify(userData),
            success: function (data) {  // Use 'data' instead of 'response'
                alert("Registration Successful!");

                // Extract token from 'data' and store it in localStorage
                if (data.token) {
                    localStorage.setItem("authToken", data.token); // Store token in localStorage
                }

                if (userData.c_password === userData.password) {
                    window.location.href = "login.html";
                } else {
                    window.location.href = "register.html"; 
                }
            },
            error: function (xhr) {
                alert("Registration failed: " + xhr.responseText);
            }
        });
    });
});