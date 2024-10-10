"use strict";
(function() {
    window.addEventListener("load", init);

    function init() {
        // Attach the login handler
        document.getElementById('login-form').addEventListener('submit', login);
    }

    function login(event) {
        event.preventDefault();  // Prevent the default form submission

        // Get form data
        let formData = {
            username: document.getElementById('username').value,
            password: document.getElementById('password').value
        };

        // Send AJAX request using fetch
        fetch('<%= request.getContextPath() %>/auth/login', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",  // Indicate that we're sending JSON
                "X-Requested-With": "XMLHttpRequest" // Indicate this is an AJAX request
            },
            body: JSON.stringify(formData)  // Convert form data to JSON
        })
            .then(statusCheck)  // Check if the response is successful
            .then(() => {
                // On success, redirect to the home page
                window.location.href = '<%= request.getContextPath() %>/home';
            })
            .catch(handleError);  // Handle errors if the request fails
    }

    async function statusCheck(res) {
        if (!res.ok) {
            // If the response is not OK, try to extract the error message
            let errorText = await res.text();
            if (!errorText) {
                errorText = `Error ${res.status}: ${res.statusText || "An error occurred"}`; // Use status text as fallback
            }

            // Create a new error object with the extracted message
            let error = new Error(errorText);
            error.status = res.status; // Add the status code to the error
            throw error;  // Throw the error so it can be caught
        }
        return res;  // Return the response if successful
    }

    function handleError(error) {
        console.error(error);

        // Provide default messages for specific error statuses
        let errorMessage;
        if (error.status === 401) {
            errorMessage = "Invalid username or password. Please try again.";
        } else if (error.status === 403) {
            errorMessage = "You don't have permission to access this resource.";
        } else if (error.status === 500) {
            errorMessage = "There was a problem with the server. Please try again later.";
        } else {
            // Default to error.message if available, or a generic message
            errorMessage = error.message || "An unexpected error occurred.";
        }

        // Display the error message to the user
        document.getElementById('error-message').innerText = errorMessage;
    }
})();