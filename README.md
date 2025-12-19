# Gedcom-Parser-Java
Projet universitaire de parsing généalogique en Java.
# Analyseur de Fichiers Généalogiques (Format GEDCOM)

Ce projet est une application développée en Java permettant de lire, d'analyser et de naviguer dans des fichiers de données généalogiques au format standard GEDCOM (.ged).

Réalisé dans le cadre d'un projet universitaire, ce logiciel charge les données familiales en mémoire et propose une interface en ligne de commande pour interroger l'arbre généalogique.

## Fonctionnalités

Le programme lit le fichier ligne par ligne, reconstruit les liens de parenté, puis permet à l'utilisateur d'effectuer les commandes suivantes :

* **INFO [Prénom Nom]** : Affiche les informations détaillées d'une personne (ID, Nom, Sexe).
* **CHILD [Prénom Nom]** : Liste tous les enfants de l'individu spécifié.
* **SIBLINGS [Prénom Nom]** : Retrouve et affiche les frères et sœurs (algorithme basé sur la famille d'origine).
* **MARRIED [Prénom1, Prénom2]** : Vérifie si deux individus sont mariés (partagent une famille de type FAMS).
* **FAMC [Prénom Nom]** : Affiche la famille d'origine (le père et la mère).

De plus, le programme effectue une validation automatique de la cohérence des données lors du chargement (vérification du sexe des parents, détection d'individus référencés mais inexistants).

## Choix Techniques

L'architecture du projet repose sur les principes de la Programmation Orientée Objet (POO) :

* **Héritage et Polymorphisme** : Utilisation d'une classe abstraite `TagInfo` (ou Entite) pour factoriser le code commun aux objets `Individu` et `Famille`.
* **Encapsulation** : Protection des données via des attributs privés et des accesseurs.
* **Structures de données dynamiques** : Utilisation de `ArrayList` pour gérer les collections d'objets dont la taille n'est pas connue à l'avance.
* **Gestion des erreurs** : Implémentation d'une hiérarchie d'exceptions personnalisées (`GedcomException`, `SexeIncoherentException`, etc.) pour un traitement fin des erreurs de logique ou de fichier.
* **Lecture optimisée** : Utilisation de `BufferedReader` pour une lecture séquentielle et performante du fichier texte.

## Structure du Projet

Les sources sont organisées comme suit :

* **Main.java** : Point d'entrée du programme. Gère l'interaction avec l'utilisateur (boucle de lecture des commandes).
* **GedcomParser.java** : Contient la logique de lecture du fichier ("parsing"), la création des objets et l'association des liens de parenté.
* **Individu.java** : Représente une personne. Contient les méthodes de recherche (trouver ses enfants, ses frères/sœurs).
* **Famille.java** : Représente une union. Fait le lien entre les conjoints et les enfants.
* **TagInfo.java** : Classe mère abstraite (sérialisable) gérant les identifiants uniques.

## Exemple d'exécution

Voici un exemple d'interaction dans la console après le chargement d'un fichier :

```text
--- LOGICIEL GENEALOGIE ---
Nom du fichier : begood.ged
Fichier chargé avec succès !

Votre commande > INFO Elizabeth II
@I1@ : Elizabeth II WINDSOR (F)

Votre commande > CHILD Elizabeth II
Enfants de Elizabeth II WINDSOR :
- Charles III
- Anne
- Andrew
- Edward

Votre commande > MARRIED Elizabeth II, Philip
OUI, Elizabeth II et Philip sont maries.
