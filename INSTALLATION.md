# Guide d'installation Docker

---

Se positionner à la racine du projet pas dans un microservice spécifique.
```bash
docker-compose up --build
```

Accès au site :
http://localhost:8086/home.html
Accès aux endpoints :
- endpoint normal précéder de '<nom>-service' => http://localhost:8080/card-service/cards 