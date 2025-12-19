package fr.univ.gedcom;

/**
 * Classe qui gère le tag SEX.
 */
public class Sexe extends TagInfo {
    private static final long serialVersionUID = 1L;

    public Sexe(String valeur) {
        // J'envoie juste la valeur (M ou F) à la classe mère TagInfo.
        // Contrairement à la classe Nom, je n'ai pas besoin de nettoyer les caractères ici.
        super(valeur);
    }
}