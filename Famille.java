package fr.univ.gedcom;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente une Famille.
 * Elle hérite d'Entite pour avoir un ID.
 */
public class Famille extends Entite {
    private static final long serialVersionUID = 1L;

    // Ici je mets des objets Individu, pas juste des String, c'est de la POO.
    private Individu mari;
    private Individu femme;
    
    // J'utilise une Liste pour les enfants car on ne sait pas combien il y en aura au début.
    private List<Individu> enfants;

    public Famille(String id) {
        super(id); // J'appelle le constructeur de la classe mère pour ranger l'ID
        
        // Attention : il faut absolument initialiser la liste ici sinon j'ai une erreur
        // NullPointerException quand j'essaie d'ajouter un enfant plus tard.
        this.enfants = new ArrayList<>(); 
    }

    public void setMari(Individu mari) {
        this.mari = mari;
    }

    public void setFemme(Individu femme) {
        this.femme = femme;
    }

    public void ajouteEnfant(Individu enfant) {
        // Je vérifie juste que l'enfant n'est pas déjà là pour éviter les doublons
        if (!this.enfants.contains(enfant)) {
            this.enfants.add(enfant);
        }
    }
    
    public List<Individu> getEnfants() {
        return enfants;
    }
    
    public Individu getMari() { return mari; }
    public Individu getFemme() { return femme; }

    @Override
    public String toString() {
        // Juste pour l'affichage dans la console
        return "Famille " + id + " (" + enfants.size() + " enfants)";
    }
}