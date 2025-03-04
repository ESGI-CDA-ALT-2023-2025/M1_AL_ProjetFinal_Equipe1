# Application DVF
Il s'agit d'une application basée sur une architecture monolithique utilisant les technologies Spring Boot et Angular conçue pour la récupération des valeurs foncières d'une zone donnée au format PDF. 
La branche [`backend`](https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1/tree/backend "backend") abrite le code source lié à la logique métier côté serveur, implémentée avec Spring Boot. 
La branche [`frontend-main`](https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1/tree/frontend-main "frontend-main") contient le code source relatif à l'interface utilisateur et à la logique côté client, développée avec Angular.  
Enfin, la branche [`conception_uml`](https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1/tree/conception_uml "conception_uml") contient le code source relatif à la conception UML de l'application, offrant une représentation visuelle des modèles et des relations entre les composants.

![Main page](https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1/blob/main/appli.jpg)

## Prérequis

[`Java 17`](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html "`Java 17`")

[`Apache Maven`](https://maven.apache.org/ "`Apache Maven`")

## Construction et Installation

**Téléchargement** :

***Avec ZIP : ***

Actuellement plus récente : [RELEASE_0.1.1](https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1/releases/tag/RELEASE_0.1.1 "RELEASE_0.1.1")
*Historique de release* : [Tags releases](https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1/tags "tags releases")

***Avec Git*** :

Clonez ce dépôt sur votre machine locale.

```bash
git clone https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1
git checkout main
cd DVF
```

**Nettoyez et installez le projet :**

```bash
mvn clean install
```

**Tests**

*Exécutez les tests unitaires :*

```bash
mvn clean test
```

*Exécutez les tests d'intégration :*

```bash
mvn clean test -Pintegration-tests
```

**Exécution de l'Application**

```bash
mvn spring-boot:run
```

L'application sera accessible à l'adresse [http://localhost:8080/](http://localhost:8080/ "http://localhost:8080/") depuis l'application cliente.

## Documentation Swagger

Explorez la documentation de l'API avec Swagger UI :
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html "http://localhost:8080/swagger-ui/index.html")

## Solution de deployment

....

### Dépendances utilisées

Spring Boot DevTools

Spring Artemis

Springdoc OpenAPI

Base de données H2

Lombok

OpenCSV

iText PDF

Commons IO


<<<<<<< HEAD
### Configuration
=======
Node.js: 18.18 (18.13 minimum requis)
>>>>>>> frontend-main

L'application utilise Spring Boot Starter Parent version 3.2.0 *(compatible **jakarta** et pas **javax**)*

Version de Java : 17

Profil pour les Tests d'Intégration

Utilisez le profil integration-tests pour l'exécution des tests d'intégration.
N'hésitez pas à personnaliser la configuration et les dépendances en fonction de vos besoins spécifiques.
