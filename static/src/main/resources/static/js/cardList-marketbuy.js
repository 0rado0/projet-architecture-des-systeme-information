let cardList = [];

document.addEventListener("DOMContentLoaded", function() {
    // Définir les informations de l'utilisateur
    setUserInfo();
    
    // Charger les cartes disponibles à l'achat
    fetchCardList();
});

async function fetchCardList() {
    try {
        // Appel à l'API pour récupérer les cartes à vendre (business to business)
        const response = await fetch('http://localhost:8080/card-service/cards');
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.status);
        }
        const data = await response.json();
        cardList = data;
        setCardlist();
    } catch (error) {
        console.error('Error fetching card list:', error);
    }
}

function onProcess(id) {
    console.log("Processing transaction for card ID:", id);
    // Cette fonction sera appelée quand l'utilisateur clique sur une carte
    // La logique d'achat pourrait être implémentée ici
}

function setCardlist() {
    // Utiliser le template existant pour afficher les cartes
    setTemplate("#cardlist", "#tableContent", cardList);
}

function setUserInfo() {
    let user = window.localStorage.getItem("user");
    user = JSON.parse(user);

    if (!user) {
        user = { username: "Guest", wallet: "0" };
    }

    document.getElementById("userNameId").innerHTML = user.username ?? "Guest";
    document.getElementById("walletId").innerHTML = user.wallet ?? "0";
}