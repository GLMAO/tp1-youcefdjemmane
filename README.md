[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/t19xNtmg)


SAID-08

# Rapport de Projet : Horloge Modulaire et Comptes à Rebours

## Description du Projet

Ce projet consiste en l'implémentation d'une application d'horloge en Java, en s'appuyant sur les principes de conception logicielle pour garantir la modularité et la maintenabilité. Le cœur du projet est une démonstration du **Design Pattern Observer**, où un service de temps central (le "Sujet") notifie des clients (les "Observateurs") de manière découplée.

L'application fournit à la fois une horloge en temps réel sur la console et une interface graphique (GUI) Swing complète, permettant à l'utilisateur de visualiser l'heure et de gérer dynamiquement plusieurs comptes à rebours.

## Fonctionnalités Principales

* **Service de Temps Centralisé** : Un `TimerService` unique fonctionne sur son propre thread, agissant comme la source de vérité pour le temps. Il émet des notifications à haute fréquence (chaque 100 millisecondes) pour une précision au dixième de seconde.
* **Architecture Observer** : Le service notifie tous les clients abonnés (`TimerChangeListener`) sans les connaître directement, permettant une architecture flexible et extensible.
* **Client Console (`Horloge`)** : Un client léger qui s'abonne au service et affiche l'heure formatée (`HH:MM:SS.T`) directement dans le terminal.
* **Client Graphique (`HorlogeGUI`)** : Une interface graphique Swing qui observe le *même* service de temps et propose :
    * Un affichage numérique de l'heure en temps réel.
    * Une gestion thread-safe des mises à jour de l'interface (via `SwingUtilities.invokeLater`).
    * Un gestionnaire de tâches permettant d'ajouter et de supprimer dynamiquement plusieurs comptes à rebours.

## Défis Techniques et Solutions

Au cours du développement, l'accent a été mis sur la robustesse et la fiabilité du service de temps.

1.  **Fiabilité des Notifications du Service**
    * **Problème** : Une analyse initiale a révélé que les notifications pour les changements de minutes (`minutesChanged()`) et d'heures (`heuresChanged()`) envoyaient des données erronées (la valeur des secondes) aux observateurs.
    * **Solution** : Le service a été corrigé pour garantir que chaque notification de propriété (`propertyChange`) transporte la charge utile (payload) correcte. Par exemple, `minutesChanged()` envoie désormais la nouvelle valeur des `minutes`, assurant ainsi que les clients reçoivent des informations cohérentes.

2.  **Gestion Robuste des Abonnements**
    * **Problème** : Le service était vulnérable aux erreurs si un client tentait de s'abonner avec une référence `null` ou si un même client s'abonnait plusieurs fois (créant des notifications en double).
    * **Solution** : Les méthodes `addTimeChangeListener()` et `removeTimeChangeListener()` ont été renforcées. Elles incluent désormais des vérifications pour ignorer les références nulles et pour empêcher l'ajout d'un observateur déjà présent dans la liste, rendant le service plus stable.

## Compilation et Exécution

Le projet est géré avec Maven. Pour compiler et lancer l'application :

```bash
# Se placer à la racine du projet
cd tp-gl-master

# Nettoyer les builds précédents et compiler le code
mvn clean compile

# Empaqueter l'application dans un JAR exécutable
mvn package

# Lancer l'application (GUI et console)
java -cp launcher/target/launcher-0.0.1-jar-with-dependencies.jar org.emp.gl.core.launcher.App


## Auteur

* DJEMMANE Youcef
* SIAD-08