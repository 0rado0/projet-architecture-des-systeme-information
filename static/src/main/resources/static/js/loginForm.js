function process(elt){
    let login = document.getElementsByName('username');
    let password = document.getElementsByName('password');
    
    let data = {"username":login[0].value, "password":password[0].value};
    const result = fetch('http://localhost:8080/auth-service/auth', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (!response.ok) {
            alert("Invalid credentials");
            throw new Error('Network response was not ok');
        }
        return response.text();
    }).then(userId => {
        if (userId) {
            // Enregistrer l'ID utilisateur dans le localStorage
            window.localStorage.setItem("user", JSON.stringify({"userId": userId, "username": null, "wallet": null}));
            // Rediriger vers la page d'accueil
            window.location.href = "home.html";
        } else {
            alert("Authentication failed: No user ID returned");
        }
    }).catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
    });
}
