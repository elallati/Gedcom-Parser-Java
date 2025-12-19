package fr.univ.gedcom;

/**
 * Classe qui représente le nom d'une personne.
 * Elle hérite de TagInfo car c'est une info comme une autre.
 */
public class Nom extends TagInfo {
    private static final long serialVersionUID = 1L;

    public Nom(String valeur) {
        // Petite astuce : dans le fichier GEDCOM, les noms de famille sont souvent écrits /Begood/.
        // J'utilise .replace() pour enlever les slashs tout de suite.
        // Comme ça, on stocke "Begood" et pas "/Begood/", c'est plus joli à l'affichage.
        super(valeur.replace("/", ""));
    }
}