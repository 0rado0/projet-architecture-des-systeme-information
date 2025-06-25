document.addEventListener("DOMContentLoaded", async function() {
    // On éxécute une requête pour savoir si on est connecté
    const user = JSON.parse(window.localStorage.getItem("user"));

    if (!user || !user.userId === null) {
        return;
    }

    const response = await fetch(`http://localhost:8080/user-service/user/${user.userId}`);

    if (!response.ok) {
        window.localStorage.removeItem("user");
        window.location.reload();
    }
});

async function setUserInfo() {
    let user = JSON.parse(window.localStorage.getItem("user"));
    
    // Vérifier si l'utilisateur existe et s'il a besoin de compléter ses informations
    if (user && user.userId && (user.username === null || user.wallet === null)) {
        try {
            const response = await fetch("http://localhost:8080/user-service/user/" + user.userId);
            if (response.ok) {
                const userData = await response.json();
                // Mise à jour des données utilisateur avec les informations complètes
                window.localStorage.setItem("user", JSON.stringify({
                    "userId": user.userId, 
                    "username": userData.username, 
                    "wallet": userData.funds
                }));
                user = JSON.parse(window.localStorage.getItem("user"));
            } else {
                console.error('Failed to fetch user data');
            }
        } catch (error) {
            console.error('Error fetching user info:', error);
        }
    }
    
    // Gérer le cas où aucun utilisateur n'est connecté
    if (!user) {
        user = { username: null, wallet: null };
    }
    
    document.getElementById("userNameId").innerHTML = user.username ?? "Guest";
    document.getElementById("walletId").innerHTML = user.wallet ?? "0";
}

setUserInfo()








