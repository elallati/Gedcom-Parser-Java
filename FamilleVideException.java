package fr.univ.gedcom;

/**
 * Exception pour le nettoyage des données.
 * Elle se déclenche si une famille ne contient personne.
 */
public class FamilleVideException extends GedcomException {
    private static final long serialVersionUID = 1L;

    public FamilleVideException(String idFamille) {
        super("Donnee Inutile : La famille " + idFamille + " est vide (ni parents, ni enfants).");
    }
}