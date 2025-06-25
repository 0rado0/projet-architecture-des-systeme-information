function process(elt) {
    
    let username = document.querySelector('input[name="username"]');
    let email = document.querySelector('input[name="email"]');
    let password = document.querySelector('input[name="password"]');
    let rePassword = document.querySelector('input[name="repassword"]');
    let termsCheckbox = document.querySelector('input[type="checkbox"]');

    if (!termsCheckbox.checked) {
        alert("Veuillez accepter les conditions d'utilisation");
        return;
    }

    if (username.value == "" || email.value == "" || password.value == "" || rePassword.value == "") {
        alert("Please fill in all fields");
        return;
    }

    if (password.value != rePassword.value){
        alert("Passwords do not match");
        return;
    }

    data = {"username": username.value, "email": email.value, "password": password.value}

    const result = fetch("http://localhost:8080/user", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (response.ok) {
            window.location.href = "loginUser.html";
            return response.json();
        } else {
            return response.text().then(text => {
                throw new Error('Échec de la création du compte: ' + text);
            });
        }
    }).catch(error => {
        alert(error.message);
        console.error('Erreur lors de la création du compte:', error);
    })
}

function check(input){
    console.log("Terms and conditions acceptance changed");
    
    // Activer ou désactiver le bouton d'envoi selon l'état de la case à cocher
    let submitButton = document.querySelector('button[type="button"]');
    submitButton.disabled = !input.checked;
    
    if (input.checked) {
        submitButton.classList.remove('disabled');
    } else {
        submitButton.classList.add('disabled');
    }
}