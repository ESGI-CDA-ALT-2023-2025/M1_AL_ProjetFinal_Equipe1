# Application Spring Boot - DVF
Il s'agit d'une application Spring Boot pour la gestion des valeurs foncières, utilisant Spring Data JPA pour la persistance des données, Spring Web pour les API RESTful. L'application comprend des fonctionnalités de traitement de fichiers, de génération de PDF et d'intégration avec un courtier de messages ActiveMQ intégré.
Elle comprend également une interface Swagger afin de visualiser les routes disponibles ainsi que leur fonctionnement.

## Prérequis

[`Java 17`](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html "`Java 17`")

[`Apache Maven`](https://maven.apache.org/ "`Apache Maven`")

## Construction et Installation

**Téléchargement** :

Clonez ce dépôt sur votre machine locale.

```bash
git clone https://github.com/ChineDmitri/M1_AL_ProjetFinal_Equipe1
git checkout backend
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

L'application sera accessible à l'adresse [http://localhost:8080/](http://localhost:8080/ "http://localhost:8080/").

**Documentation Swagger**

Explorez la documentation de l'API avec Swagger UI :
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html "http://localhost:8080/swagger-ui/index.html")

### Dépendances utilisées

Spring Boot DevTools

Spring Artemis

Springdoc OpenAPI

Base de données H2

Lombok

OpenCSV

iText PDF

Commons IO


### Configuration

L'application utilise Spring Boot Starter Parent version 3.2.0 *(compatible **jakarta** et pas **javax**)*

Version de Java : 17

Profil pour les Tests d'Intégration

Utilisez le profil integration-tests pour l'exécution des tests d'intégration.
N'hésitez pas à personnaliser la configuration et les dépendances en fonction de vos besoins spécifiques.