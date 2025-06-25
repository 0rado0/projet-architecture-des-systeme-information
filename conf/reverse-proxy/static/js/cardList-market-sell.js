let cardList = [];

document.addEventListener("DOMContentLoaded", function() {
    let user = window.localStorage.getItem("user");
    user = JSON.parse(user);

    if (!user) {
        user = { username: null, wallet: null };
        return;
    }

    fetchUserCards(user["userId"]);
});

// Récupère toutes les cartes de l'utilisateur qui ne sont pas déjà sur le marché
async function fetchUserCards(userId) {
    try {
        const response = await fetch(`http://localhost:8080/cards/user/${userId}`);
        const data = await response.json();
        
        // Filtrer pour n'afficher que les cartes de l'utilisateur qui ne sont pas déjà sur le marché
        cardList = data.filter(card => !card.onMarket);
        setCardlist();
    } catch (error) {
        console.error('Error fetching user cards:', error);
    }
}

// Fonction appelée lorsque l'utilisateur clique sur le bouton de vente d'une carte
function onProcess(id){
    console.log("Selling card with ID: " + id);
    
    // Récupérer l'utilisateur connecté
    let user = window.localStorage.getItem("user");
    if (!user) {
        alert("Veuillez vous connecter pour vendre une carte");
        return;
    }
    
    user = JSON.parse(user);
    const userId = user.userId;
    
    if (!userId) {
        alert("Veuillez vous connecter pour vendre une carte");
        return;
    }
    
    // Créer l'objet d'ordre de vente
    const sellOrder = {
        userId: userId,
        cardId: id
    };
    
    // Envoyer la requête de vente
    fetch('http://localhost:8080/shop/sell', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(sellOrder)
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Échec de la mise en vente');
        }
    })
    .then(data => {
        alert(data);
        // Ajouter un petit délai pour s'assurer que le serveur a terminé le traitement
        setTimeout(() => {
            // Rafraîchir la liste des cartes de l'utilisateur
            fetchUserCards(userId);
            // Mettre à jour les informations de l'utilisateur
            updateUserInfo();
            // Proposer à l'utilisateur d'aller voir le marché
            if (confirm("Carte mise en vente avec succès ! Voulez-vous consulter le marché ?")) {
                window.location.href = "cardList-market-buy.html";
            } else {
                // Recharger la page actuelle si l'utilisateur ne souhaite pas naviguer
                window.location.reload();
            }
        }, 300); // Délai de 300ms
    })
    .catch(error => {
        alert("Erreur lors de la mise en vente : " + error.message);
        console.error('Error selling card:', error);
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

// Mettre à jour les informations de l'utilisateur après une vente
async function updateUserInfo() {
    // Récupérer les informations à jour de l'utilisateur depuis le serveur
    let user = window.localStorage.getItem("user");
    if (!user) return;
    
    user = JSON.parse(user);
    const userId = user.userId;
    
    if (!userId) return;
    
    try {
        const response = await fetch(`http://localhost:8080/user/${userId}`);
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
