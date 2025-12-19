package fr.univ.gedcom;
import java.io.Serializable;

/**
 * Classe abstraite qui sert de modèle pour les infos (Nom, Sexe...).
 * Le prof voulait des Objets pour chaque tag, donc j'ai créé cette classe mère.
 */
// "abstract" car on n'utilise jamais un TagInfo tout seul, c'est soit un Nom, soit un Sexe.
public abstract class TagInfo implements Serializable {
    // Toujours le numéro de version pour la sauvegarde (sérialisation)
    private static final long serialVersionUID = 1L;
    
    // C'est ici que je stocke la vraie donnée (ex: "John" ou "M").
    // Je l'ai mis en "protected" pour que les classes filles (Nom et Sexe) puissent s'en servir.
    protected String valeur;

    public TagInfo(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
    
    @Override
    public String toString() {
        // Quand j'affiche l'objet, je veux voir le texte directement
        return valeur;
    }
}