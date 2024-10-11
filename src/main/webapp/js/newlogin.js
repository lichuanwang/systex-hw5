"use strict";
(function() {
    window.addEventListener("load", init);

    function init() {
        // Attach the login handler
        console.log("hello, this is newlogin.js");
        const loginForm = document.getElementById('login-form');
        const loginMethodSelect = document.getElementById('login-method');

        // Dynamically handle the form submission based on the chosen method
        loginMethodSelect.addEventListener('change', function () {
            const selectedMethod = loginMethodSelect.value;

            if (selectedMethod === 'traditional') {
                // Dynamically add action and method attributes for traditional submission
                loginForm.setAttribute('action', contextPath + '/auth/login');
                loginForm.setAttribute('method', 'POST');
            } else {
                // Remove action and method attributes for AJAX submission
                loginForm.removeAttribute('action');
                loginForm.removeAttribute('method');
            }
        });

        loginForm.addEventListener('submit', login);
    }

    function login(event) {
        // Prevent the form from submitting traditionally when AJAX is selected
        const selectedMethod = document.getElementById('login-method').value;

        if (selectedMethod === 'ajax') {
            event.preventDefault();
            console.log("Using AJAX");

            let formData = {
                username: document.getElementById('username').value,
                password: document.getElementById('password').value
            };

            fetch(contextPath + '/auth/login', {  // Use the dynamically assigned contextPath
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: JSON.stringify(formData)
            })
                .then(statusCheck)
                .then(() => {
                    console.log("Login successful, redirecting...");

                    // Delay redirection by 2 seconds
                    setTimeout(() => {
                        window.location.href = contextPath + '/home';
                    }, 2000); // 2 seconds delay before redirection
                })
                .catch(handleError);
        } else {
            // Allow traditional form submission
            console.log("Using traditional form submission");
        }
    }

    async function statusCheck(res) {
        if (!res.ok) {
            let errorText = await res.text();
            let error = new Error(errorText);
            error.status = res.status; // Explicitly set the status code
            throw error;
        }
        return res;
    }

    function handleError(error) {
        console.error(error);
        document.getElementById('error-message').innerText = error.message;
    }
})();
