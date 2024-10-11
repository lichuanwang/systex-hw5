"use strict";
(function() {
    window.addEventListener("load", init);

    function init() {
        console.log("hello, this is newlogin.js");
        const loginForm = document.getElementById('login-form');
        const loginMethodSelect = document.getElementById('login-method');

        // Restore saved login method from localStorage
        const savedMethod = localStorage.getItem('loginMethod');
        if (savedMethod) {
            loginMethodSelect.value = savedMethod;
            if (savedMethod === 'traditional') {
                // Set action and method if "traditional" was saved
                loginForm.setAttribute('action', contextPath + '/auth/login');
                loginForm.setAttribute('method', 'POST');
            }
        }

        // Update form action and method dynamically when login method changes
        loginMethodSelect.addEventListener('change', function () {
            const selectedMethod = loginMethodSelect.value;
            localStorage.setItem('loginMethod', selectedMethod);

            if (selectedMethod === 'traditional') {
                // Set action and method for traditional form submission
                loginForm.setAttribute('action', contextPath + '/auth/login');
                loginForm.setAttribute('method', 'POST');
            } else {
                // Remove action and method for AJAX submission
                loginForm.removeAttribute('action');
                loginForm.removeAttribute('method');
            }
        });

        loginForm.addEventListener('submit', login);
    }

    function login(event) {
        const selectedMethod = document.getElementById('login-method').value;

        if (selectedMethod === 'ajax') {
            event.preventDefault(); // Prevent traditional form submission for AJAX

            let formData = {
                username: document.getElementById('username').value,
                password: document.getElementById('password').value
            };

            fetch(contextPath + '/auth/login', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: JSON.stringify(formData)
            })
                .then(statusCheck)
                .then(() => {
                    window.location.href = contextPath + '/home';
                })
                .catch(handleError);
        }
    }

    async function statusCheck(res) {
        if (!res.ok) {
            throw await res.json();
        }
        return res;
    }

    function handleError(error) {
        console.error(error);
        // Display only the error message from the JSON response
        document.getElementById('error-message').innerText = error.message || "An error occurred. Please try again.";
        document.getElementById('password').value = "";
    }
})();
