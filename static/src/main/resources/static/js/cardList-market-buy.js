let cardList = [];

document.addEventListener("DOMContentLoaded", function() {
    let user = window.localStorage.getItem("user");
    user = JSON.parse(user);

    if (!user) {
        user = { username: null, wallet: null };
        return;
    }

    fetchMarketCards();
});

// Récupère les cartes disponibles à l'achat (celles mises en vente sur le marché)
async function fetchMarketCards() {
    try {
        const response = await fetch(`http://localhost:8080/shop-service/shop/cards`);
        const data = await response.json();
        
        // Filtrer pour n'afficher que les cartes qui sont sur le marché
        cardList = data;
        setCardlist();
    } catch (error) {
        console.error('Error fetching market cards:', error);
    }
}

// Fonction appelée lorsque l'utilisateur clique sur le bouton d'achat d'une carte
function onProcess(id){
    console.log("Buying card with ID: " + id);
    
    // Récupérer l'utilisateur connecté
    let user = window.localStorage.getItem("user");
    if (!user) {
        alert("Veuillez vous connecter pour acheter une carte");
        return;
    }
    
    user = JSON.parse(user);
    const userId = user.userId;
    
    if (!userId) {
        alert("Veuillez vous connecter pour acheter une carte");
        return;
    }
    
    // Créer l'objet d'ordre d'achat
    const buyOrder = {
        userId: userId,
        cardId: id
    };
    
    // Envoyer la requête d'achat
    fetch('http://localhost:8080/shop-service/shop/buy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(buyOrder)
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Échec de l\'achat');
        }
    })
    .then(data => {
        alert(data);
        // Ajouter un petit délai pour s'assurer que le serveur a terminé le traitement
        setTimeout(() => {
            // Rafraîchir la liste des cartes disponibles
            fetchMarketCards();
            // Mettre à jour les informations de l'utilisateur (solde, etc.)
            updateUserInfo();
            // Proposer à l'utilisateur d'aller voir ses cartes
            if (confirm("Carte achetée avec succès ! Voulez-vous consulter vos cartes ?")) {
                window.location.href = "cardList-market-sell.html";
            } else {
                // Recharger la page actuelle si l'utilisateur ne souhaite pas naviguer
                window.location.reload();
            }
        }, 300); // Délai de 300ms
    })
    .catch(error => {
        alert("Erreur lors de l'achat : " + error.message);
        console.error('Error buying card:', error);
    });
}

function setCardlist(){
    setTemplate("#cardlist","#tableContent",cardList)
}

function setUserInfo() {
    let user = window.localStorage.getItem("user");
    user = JSON.parse(user);

    if (!user) {
        user = { username: null, wallet: null };
    }

    document.getElementById("userNameId").innerHTML= user.username ?? "Guest";
    document.getElementById("walletId").innerHTML= user.wallet ?? "0";
}

// Mettre à jour les informations de l'utilisateur après un achat
async function updateUserInfo() {
    // Récupérer les informations à jour de l'utilisateur depuis le serveur
    let user = window.localStorage.getItem("user");
    if (!user) return;
    
    user = JSON.parse(user);
    const userId = user.userId;
    
    if (!userId) return;
    
    try {
        const response = await fetch(`http://localhost:8080/user-service/user/${userId}`);
        if (response.ok) {
            const userData = await response.json();
            
            // Mettre à jour les informations dans le localStorage
            user.wallet = userData.funds;
            window.localStorage.setItem("user", JSON.stringify(user));
            
            // Mettre à jour l'affichage
            setUserInfo();
        }
    } catch (error) {
        console.error('Error updating user info:', error);
    }
}

setUserInfo();
setCardlist();
