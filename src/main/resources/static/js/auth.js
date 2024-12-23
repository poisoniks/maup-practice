function registerUser() {
    const user = {
        firstname: document.getElementById('firstname').value,
        lastname: document.getElementById('lastname').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
    .then(response => {
        if (response.ok) {
            alert('Registration successful! Please login.');
            window.location.href = '/login.html';
        } else {
            response.text().then(error => alert(`Error: ${error}`));
        }
    })
    .catch(error => console.error('Error during registration:', error));
}

function loginUser() {
    const credentials = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
    };

    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    })
    .then(response => {
        if (response.ok) {
            alert('Login successful!');
            window.location.href = '/';
        } else {
            alert('Invalid credentials.');
        }
    })
    .catch(error => console.error('Error during login:', error));
}
