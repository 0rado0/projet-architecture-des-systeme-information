# On utilise la dernière version de l'image Nginx
FROM nginx:latest

# On copie les fichiers statiques vers le répertoire par défaut de Nginx
COPY static/ /usr/share/nginx/html/

# On copie le fichier de configuration Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# On expose le port 8080 pour accéder à l'application
EXPOSE 80

# Commande pour démarrer Nginx
CMD ["nginx", "-g", "daemon off;"]
