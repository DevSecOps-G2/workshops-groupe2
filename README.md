# Projet DevSecOps : sécurisation de l'application LiveChat

- **Module** : DevSecOps, Master DevOps et Cloud Computing
- **Université** : Abdelmalek Essaâdi, Faculté Polydisciplinaire de Larache
- **Année universitaire** : 2026/2027
- **Encadrant** : Mohamed KOUISSI
- **Groupe** : G2
- **Membres** :
  - Hajar EL BACHIRI
  - Keltouma ACHGUER
  - Mohamed El Amine HOUMADA
- **Application analysée** : LiveChat, application web de messagerie en temps réel en Java/Maven (Jakarta EE, JSP, WebSocket, Hibernate, MySQL)

## Présentation du projet

Ce dépôt regroupe les workshops du module DevSecOps. Ils sont tous réalisés sur **une seule application, LiveChat**. À chaque workshop, nous ajoutons une couche de sécurité différente, afin de construire progressivement une démarche DevSecOps complète, du code source jusqu'au déploiement et à l'exploitation.

Le principe suivi est le *Shift Left* : détecter les problèmes de sécurité le plus tôt possible dans le cycle de développement, grâce à des outils automatisés intégrés dans un pipeline CI/CD (GitHub Actions).

| Workshop | Thème | Type de test | Outils | Ce qui est analysé |
|---|---|---|---|---|
| 2 | Sécurité du code source | SAST | SonarQube, Semgrep | Le code source |
| 3 | Sécurité des dépendances | SCA | Dependabot, OWASP Dependency-Check | Les bibliothèques tierces |
| 4 | Détection automatique des vulnérabilités web | DAST | OWASP ZAP | L'application en exécution |

*Ce tableau et le sommaire sont complétés au fur et à mesure des workshops suivants.*

Les approches des workshops 2 à 4 sont complémentaires : le SAST analyse ce que nous écrivons, le SCA analyse ce que nous importons, et le DAST teste ce que nous déployons.

## Sommaire

- [Workshop 2 : Analyse de la qualité et de la sécurité du code (SAST)](#workshop2)
- [Workshop 3 : Sécurité des dépendances (SCA)](#workshop3)
- [Workshop 4 : Détection automatique des vulnérabilités web (DAST)](#workshop4)
<!-- Ajouter ici les prochains workshops -->

---

<a id="workshop2"></a>

## Workshop 2 : Analyse de la qualité et de la sécurité du code (SAST)

### 1. Présentation du projet

Ce workshop a pour objectif d'analyser la qualité et la sécurité du code source à l'aide de SonarQube et de Semgrep, puis d'intégrer une analyse automatisée de sécurité dans un pipeline CI avec GitHub Actions.

Les objectifs sont :
- Détecter les problèmes de qualité et les vulnérabilités potentielles.
- Analyser le code source avec deux outils complémentaires.
- Automatiser l'analyse de sécurité à chaque modification du dépôt.
- Identifier et corriger les problèmes détectés.

### 2. Outils et technologies utilisés

- **Git et GitHub** : gestion du code source et du dépôt.
- **SonarQube** : analyse de la qualité du code et détection des problèmes.
- **Semgrep** : analyse statique du code et détection des problèmes de sécurité.
- **GitHub Actions** : automatisation de l'analyse dans un pipeline CI.
- **Docker** : exécution de SonarQube et Semgrep dans un conteneur.

### 3. Analyse avec SonarQube

SonarQube a été utilisé pour analyser le projet et examiner les indicateurs de qualité et les problèmes détectés.

Cette analyse permet notamment d'identifier :
- Les bugs potentiels.
- Les vulnérabilités de sécurité.
- Les code smells.
- Les indicateurs de qualité du projet.

#### Captures d'écran

![Tableau de bord SonarQube](screenshots/sonarqube-dashboard.png)

*Figure 1 : Tableau de bord général de SonarQube.*

![Problèmes détectés par SonarQube](screenshots/sonarqube-Security-issues.png)
*Figure 2 : Résultats et problèmes de sécurité détectés par SonarQube.*

![Problèmes détectés par SonarQube](screenshots/sonarqube-Reliability-issues.png)
*Figure 3 : Résultats et problèmes de fiabilité détectés par SonarQube.*

![Problèmes détectés par SonarQube](screenshots/sonarqube-Maintainability-issues.png)
*Figure 4 : Résultats et problèmes de maintenabilité détectés par SonarQube.*

### 4. Analyse de sécurité avec Semgrep

Semgrep a été utilisé pour effectuer une analyse statique du code source.

La commande utilisée est :

```bash
docker run --rm \
  --mount "type=bind,source=$($PWD.Path),target=/src" \
  -w /src \
  semgrep/semgrep \
  semgrep scan --config auto .
```

Lors de l'analyse, Semgrep a examiné les fichiers suivis par Git et a notamment signalé :

<!-- nosemgrep: javascript.lang.security.detect-insecure-websocket.detect-insecure-websocket -->
- Une utilisation de `ws://` dans `src/main/webapp/chat.jsp`, correspondant à une connexion WebSocket non chiffrée.
- Une référence de version mutable `actions/checkout@v4` dans le workflow GitHub Actions.

Ces résultats ont permis d'identifier des améliorations possibles concernant la sécurisation des communications et la chaîne d'approvisionnement logicielle.

#### Captures d'écran

![Résultats de l'analyse 1 Semgrep](screenshots/semgrep-scan1.png)

*Figure 5 : Résultats de l'analyse statique avec Semgrep avant correction.*

![Résultats de l'analyse 2 Semgrep](screenshots/semgrep-scan2.png)

*Figure 6 : Résultats de l'analyse statique avec Semgrep après correction.*

![Résultats de l'analyse 3 Semgrep](screenshots/semgrep-scan3.png)

*Figure 7 : Résultats de l'analyse statique avec Semgrep après re-correction.*

### 5. Intégration dans un pipeline CI

Un workflow GitHub Actions a été créé dans le fichier :

`.github/workflows/semgrep.yml`

Le pipeline est déclenché lors :
- D'un `push` sur la branche `main`.
- D'une pull request ciblant `main`.
- D'un lancement manuel du workflow.

Le pipeline récupère le code source, exécute Semgrep et utilise le résultat de l'analyse pour déterminer si l'étape de sécurité réussit ou échoue.

Cette intégration permet de détecter automatiquement certains problèmes de sécurité au cours du développement.

#### Capture d'écran

![Pipeline CI GitHub Actions](screenshots/github-actions-pipeline.png)

*Figure 8 : Intégration et exécution du pipeline CI sur GitHub Actions.*

### 6. Corrections et vérifications

Les analyses ont permis d'identifier des problèmes potentiels dans le code applicatif et dans la configuration du pipeline.

Les actions envisagées ou réalisées comprennent :
- L'adaptation du protocole WebSocket pour utiliser une connexion sécurisée en production avec HTTPS/WSS.
- Le verrouillage de l'action GitHub `actions/checkout` sur un SHA de commit précis.
- La relance de l'analyse Semgrep après les modifications.
- La vérification de la compilation, des tests et du fonctionnement de l'application.

Les corrections sont considérées comme validées après vérification des résultats des analyses et des tests.

### 7. Conclusion

L'utilisation combinée de SonarQube, Semgrep et GitHub Actions permet d'améliorer le suivi de la qualité et de la sécurité du projet.

SonarQube fournit une vue d'ensemble de la qualité du code, tandis que Semgrep permet de détecter des problèmes de sécurité potentiels. L'intégration de Semgrep dans un pipeline CI automatise ces vérifications et facilite la détection précoce des problèmes lors des modifications du code.

Cette réalisation constitue une première étape vers une démarche d'intégration continue intégrant des contrôles de qualité et de sécurité.

---

<a id="workshop3"></a>

## Workshop 3 : Sécurité des dépendances (SCA)

**Dependabot et OWASP Dependency-Check**

- **Dépôt d'origine du workshop** : https://github.com/MohamedElAmineHoumada/workshop3-G2

### 1. Objectif

Sécuriser les dépendances d'un projet Maven en utilisant :
- **Dependabot** : détection des dépendances vulnérables et mises à jour automatiques via des Pull Requests.
- **OWASP Dependency-Check** : analyse SCA qui compare les dépendances à la base NVD pour détecter les CVE.

Ces outils s'intègrent dans un pipeline CI/CD (GitHub Actions) pour appliquer le principe du *Shift Left*.

### 2. Définitions

|         Terme          |                                  Définition                                  |
|------------------------|------------------------------------------------------------------------------|
| Dépendance             | Bibliothèque externe utilisée par le projet, déclarée dans `pom.xml`         |
| Dépendance transitive  | Dépendance non déclarée directement, apportée par une autre dépendance       |
| CVE                    | Identifiant public d'une faille de sécurité connue                           |
| CVSS                   | Score de gravité d'une CVE, de 0 à 10                                        |
| SCA                    | Software Composition Analysis : analyse des composants tiers                 |
| NVD                    | National Vulnerability Database : base publique des CVE (NIST)               |
| Dependabot             | Outil GitHub qui alerte et propose des mises à jour de dépendances           |
| OWASP Dependency-Check | Outil SCA qui détecte les dépendances ayant des CVE connues                  |
| Pull Request (PR)      | Proposition de modification à relire avant fusion dans la branche principale |

### 3. Environnement de travail

Outils utilisés :
- Git, GitHub (dépôt + GitHub Actions)
- Java 26 et Maven 3.9.16 (via mvnd 1.0.6) en local ; le workflow GitHub Actions utilise Java 17 (Temurin), installé automatiquement
- Éditeur : Visual Studio Code

![Versions des outils](screenshots/01-outils.png)

### 4. Mise en place du dépôt

Le dépôt `workshop3-G2` a été créé sur GitHub et alimenté avec le code du projet Live Chat. Le `pom.xml` déclare les dépendances suivantes :

|                      Dépendance                      |          Rôle           |
|------------------------------------------------------|-------------------------|
| Jakarta EE (Servlet, JSP/JSTL, WebSocket, JSON, EL)  | API web et temps réel   |
| `jakarta.persistence-api` / `hibernate-core`         | Accès aux données (ORM) |
| `mysql-connector-j` 8.0.33 (initialement)            | Connexion à MySQL       |
| `gson` 2.12.1                                        | Sérialisation JSON      |
| `junit-jupiter` 5.10.2                               | Tests                   |

> Versions du `pom.xml` initial. `mysql-connector-j` a ensuite été mis à jour en 8.2.0 (voir section 9).

![Code du projet](screenshots/03-depot-code.png)

### 5. Pipeline CI : build Maven

Le workflow `.github/workflows/ci.yml` se déclenche à chaque `push` et pull request vers `main`, ainsi qu'à la demande (`workflow_dispatch`). Étapes :

1. **Checkout** du code.
2. **Set up JDK 17** (Temurin) avec cache Maven.
3. **Build with Maven** (`mvn clean verify`).
4. **Restore** du cache de la base de vulnérabilités.
5. **Run OWASP Dependency-Check** (voir section 10), avec clé API NVD et miroir de données.
6. **Save** du cache de la base, même en cas d'échec de l'étape précédente.
7. **Upload** du rapport (HTML et JSON) comme artefact du run.

![Fichier ci.yml](screenshots/04-ci-workflow.png)

![Build Maven réussi](screenshots/05-build-maven.png)

### 6. Dependabot : configuration

Le fichier `.github/dependabot.yml` définit ce que Dependabot surveille.

```yaml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/"
    schedule:
      interval: "weekly"
```

|      Paramètre      |                              Rôle                              |
|---------------------|----------------------------------------------------------------|
| `version: 2`        | Version du format de configuration                             |
| `package-ecosystem` | Type de dépendances à surveiller (ici `maven`)                 |
| `directory`         | Dossier où se trouve le fichier de dépendances (`/` = racine)  |
| `schedule.interval` | Fréquence de vérification (ici `weekly`, une fois par semaine) |

Cette configuration est à l'origine des 5 Pull Requests présentées en section 8.

![dependabot.yml](screenshots/06-dependabot-yml.png)

### 7. Dependabot Alerts : état initial

Nous avons activé **Dependency graph** et **Dependabot alerts** dans *Settings → Advanced Security*. GitHub détecte **1 alerte** de gravité **High** :

|         Dépendance         |                 Alerte                  | Gravité |  Fichier  |
|----------------------------|-----------------------------------------|---------|-----------|
| `mysql-connector-j` 8.0.33 | MySQL Connectors takeover vulnerability | High    | `pom.xml` |

Cette alerte correspond à la CVE-2023-22102 : la version utilisée du connecteur MySQL contient une faille connue, corrigée à partir de la version 8.2.0 (voir section 9).

![Alerte Dependabot](screenshots/07-alertes-avant.png)

### 8. Pull Requests de Dependabot

Dependabot a ouvert **5 PRs** de mise à jour. Chacune comporte une description (avec un indicateur de compatibilité) et propose la modification du `pom.xml`.

![Liste des PRs](screenshots/08-liste-prs.png)

![Détail d'une PR](screenshots/09-pr-detail.png)

#### Analyse

| PR |                 Mise à jour                  |    Type     | Conflit |  Décision |
|----|----------------------------------------------|-------------|---------|-----------|
| #1 | `jakarta.persistence-api` 3.1.0 → 3.2.0      | Mineure     | Aucun   | Fusionnée |
| #2 | `jakarta.servlet.jsp.jstl-api` 3.0.0 → 3.0.2 | Correctif   | Aucun   | Fusionnée |
| #3 | `maven-compiler-plugin` 3.11.0 → 3.16.0      | Mineure     | Aucun   | Fusionnée |
| #4 | `jakarta.servlet.jsp-api` 3.1.0 → 4.0.0      | **Majeure** | Aucun   | Fusionnée |
| #5 | `jakarta.websocket-api` 2.1.0 → 2.2.0        | Mineure     | Aucun   | Fusionnée |

Règle appliquée : une PR est fusionnée si elle n'a pas de conflit avec `main`. Une mise à jour **majeure** (comme la PR #4, `jakarta.servlet.jsp-api` 3.1.0 → 4.0.0) peut casser la compatibilité (API changée, serveur d'application incompatible) : elle doit être testée après fusion.

Les 5 PRs ouvertes par Dependabot ont été fusionnées une par une dans `main`. Après les fusions, l'onglet *Pull requests* affiche **0 PR ouverte et 5 fermées**.

Après les fusions et la mise à jour de `mysql-connector-j`, le pipeline GitHub Actions s'exécute avec succès (job `build-and-scan` au vert). La montée de version majeure de `jakarta.servlet.jsp-api` n'a pas fait échouer le build.

![PR fusionnée](screenshots/10-pr-fusionnee.png)

![Toutes les PRs fusionnées (Open 0, Closed 5)](screenshots/10b-prs-fusionnees.png)

### 9. Résultat après fusion

Les PRs ci-dessus sont des *version updates* : elles maintiennent les dépendances à jour mais ne ciblent pas spécifiquement la dépendance vulnérable. Après leur fusion, l'alerte sur `mysql-connector-j` restait donc ouverte. Deux solutions existent :
- activer les **Dependabot security updates** (*Settings → Advanced Security*) : Dependabot ouvre alors une PR de correction pour l'alerte ;
- ou modifier manuellement la version dans `pom.xml` vers une version corrigée.

Nous avons choisi la correction manuelle : `mysql-connector-j` 8.0.33 → **8.2.0** (version qui corrige la CVE-2023-22102), commit « Update mysql-connector-j to 8.2.0 (CVE-2023-22102) ». Après le push, le pipeline est passé au vert et l'alerte est **fermée** (onglet Dependabot : Open 0, Closed 1).

![Alertes après correction](screenshots/11-alertes-apres.png)

### 10. OWASP Dependency-Check

#### 10.1 Principe

Le plugin Maven `dependency-check-maven` (version 13.0.0) analyse les dépendances du projet, les compare à la base NVD et produit un rapport (HTML et JSON) listant les CVE détectées avec leur score CVSS.

#### 10.2 Clé API NVD et miroir de données

L'analyse télécharge toute la base NVD (plus de 400 000 enregistrements). Sans clé API, le téléchargement est très lent et limité. Nous avons donc :
- demandé une clé gratuite sur https://nvd.nist.gov/developers/request-an-api-key, activée par email, puis stockée dans un **secret GitHub** `NVD_API_KEY` (*Settings → Secrets and variables → Actions*). La clé n'apparaît jamais dans le code ni dans les logs ;
- configuré l'option `nvdDatafeedUrl` pour récupérer la base depuis le **miroir quotidien** publié par le projet Dependency-Check. L'API NVD n'est alors sollicitée que pour les mises à jour récentes, avec la clé.

![Secret NVD_API_KEY](screenshots/12-secret-nvd.png)

#### 10.3 Problèmes rencontrés et solutions

|                  Problème                  |                                          Cause                                          |                              Solution                               |
|--------------------------------------------|-----------------------------------------------------------------------------------------|---------------------------------------------------------------------|
| `Invalid API Key, length of 0`             | Aucune clé fournie : le client NVD échouait sans message clair                          | Création du secret `NVD_API_KEY`, deux steps conditionnels          |
| `Invalid API Key: xxxxx-*****-xxxxx`       | Le code de confirmation de l'email avait été utilisé à la place de la clé, non activée  | Activation de la clé, puis mise à jour du secret avec la vraie clé  |
| `No documents exist`                       | Conséquence de l'échec du téléchargement de la base NVD                                 | Disparaît une fois la base téléchargée                              |
| `Retrying request ...` (très nombreuses)   | API NVD surchargée : seulement 7 % de la base téléchargée après plus de 4 h             | Annulation du run, utilisation du miroir `nvdDatafeedUrl`           |
| Avertissements Node 20 / `setup-java@v4`   | Actions obsolètes                                                                       | Passage à `actions/checkout@v5` et `actions/setup-java@v5`          |

Après ces corrections, le job `build-and-scan` se termine avec succès : environ 1 min 40 s lors du premier run (téléchargement de la base depuis le miroir), puis environ 40 s grâce au cache.

#### 10.4 Résultats de l'analyse

Rapport généré le 21 octobre 2026 (run #16, après la mise à jour de `mysql-connector-j`) :

- Dépendances analysées : 30
- Dépendances vulnérables : 1
- Vulnérabilités trouvées : 1
- Répartition par gravité : Critical 0, High 1, Medium 0, Low 0

|       Dépendance       |      CVE      |          CVSS           | Gravité |                      Action                       |
|------------------------|---------------|-------------------------|---------|---------------------------------------------------|
| `protobuf-java` 3.21.9 | CVE-2024-7254 | 7.5 (v3.1), 8.7 (v4.0)  | High    | Mise à jour vers 3.25.5 ou supérieur conseillée   |

`protobuf-java` est une **dépendance transitive** : elle n'est pas déclarée dans notre `pom.xml`, mais elle est apportée par `mysql-connector-j` 8.2.0. La CVE-2024-7254 permet un déni de service (dépassement de pile) lorsque l'application analyse des données Protocol Buffers non fiables. La correction consiste à surcharger la version de `protobuf-java` dans le `pom.xml` (3.25.5 ou supérieure).

Le rapport confirme que la CVE-2023-22102 de `mysql-connector-j` a bien disparu après la mise à jour en 8.2.0.

![Run CI réussi](screenshots/13-ci-run-vert.png)

![Artefact du rapport](screenshots/14-artefact.png)

![Rapport HTML](screenshots/15-rapport-html.png)

#### 10.5 Seuil d'échec

Le build utilise `-DfailBuildOnCVSS=11` : il n'échoue jamais à cause des CVE (le score maximal est 10). Cette valeur est adaptée au contexte pédagogique. En production, on utiliserait par exemple `7` pour bloquer le build dès qu'une vulnérabilité High ou Critical est détectée.

### 11. Comparaison et conclusion

|         Critère         |                 Dependabot                 |            OWASP Dependency-Check            |
|-------------------------|--------------------------------------------|----------------------------------------------|
| Rôle                    | Alerter et proposer des mises à jour       | Scanner et produire un rapport de CVE        |
| Déclenchement           | Planifié (`schedule`) + alertes continues  | À chaque push / PR dans le pipeline          |
| Action sur le code      | Ouvre des Pull Requests                    | Aucune (rapport uniquement)                  |
| Base de vulnérabilités  | GitHub Advisory Database                   | NVD (NIST)                                   |
| Mise en place           | Fichier `dependabot.yml` + réglages GitHub | Plugin Maven + clé API NVD + workflow        |

Les deux outils sont **complémentaires** : Dependabot corrige en proposant des mises à jour, Dependency-Check vérifie à chaque build l'état des dépendances. Leur intégration dans la CI applique le principe du *Shift Left* : les vulnérabilités sont détectées tôt, avant la mise en production.

**Bilan du groupe**
- Dependabot est simple à mettre en place et réduit le travail de mise à jour, mais ses PRs de type *version update* ne ciblent pas forcément la dépendance vulnérable : il faut activer les *security updates* ou corriger manuellement.
- Les mises à jour majeures (comme `jakarta.servlet.jsp-api` 4.0.0) demandent une vérification avant fusion.
- OWASP Dependency-Check dépend fortement de la disponibilité de la base NVD : la clé API est indispensable, et un miroir de données ou un cache permet d'éviter des téléchargements très longs.
- Lire attentivement les logs a permis de distinguer une erreur de configuration (clé absente, clé non activée) d'un problème externe (API NVD lente).
- Dependency-Check a détecté une vulnérabilité dans une dépendance transitive (`protobuf-java`), que Dependabot n'avait pas signalée dans notre dépôt : les deux outils se complètent.

### 12. Références

- Dependabot : https://docs.github.com/en/code-security/dependabot
- OWASP Dependency-Check : https://jeremylong.github.io/DependencyCheck/
- Plugin Maven : https://jeremylong.github.io/DependencyCheck/dependency-check-maven/
- NVD : https://nvd.nist.gov/

---

<a id="workshop4"></a>

## Workshop 4 : Détection automatique des vulnérabilités web (DAST)

**OWASP ZAP**

*Section en cours de rédaction.*