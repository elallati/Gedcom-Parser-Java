package fr.univ.gedcom;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une personne (Individu).
 * Elle hérite de Entite car elle a un identifiant unique (@I1@).
 */
public class Individu extends Entite {
    private static final long serialVersionUID = 1L;

    // J'utilise des objets spécifiques (Nom, Sexe) au lieu de simples String
    // pour respecter la consigne "Tout doit être un objet".
    private Nom nom;
    private Nom prenom;
    private Sexe sexe;
    
    // C'est mon astuce pour gérer les références manquantes (Lazy Loading).
    // Si ce booléen est vrai, c'est que l'individu a été cité mais pas encore défini.
    private boolean estFantome = false;
    
    // Liens de parenté
    private Famille familleOrigine;      // FAMC (La famille où je suis né)
    private List<Famille> famillesCrees; // FAMS (Les familles où je suis parent)

    public Individu(String id) {
        super(id); // J'appelle la classe mère Entite pour stocker l'ID
        
        // J'initialise tout ici pour éviter les NullPointerException plus tard
        this.famillesCrees = new ArrayList<>();
        this.nom = new Nom("Inconnu"); // Valeur par défaut
        this.prenom = new Nom("");
        this.sexe = new Sexe("U");     // U pour Unknown (Inconnu)
    }

    // --- Gestion des Fantômes ---
    public void setFantome(boolean b) {
        this.estFantome = b;
    }
    
    public boolean isFantome() {
        return estFantome;
    }

    // --- Setters (Modification des infos) ---
    public void setNom(String val) { this.nom = new Nom(val); }
    public void setPrenom(String val) { this.prenom = new Nom(val); }
    public void setSexe(String val) { this.sexe = new Sexe(val); }
    
    public void setFamilleOrigine(Famille f) { this.familleOrigine = f; }

    // Quand on ajoute une famille où je suis parent, je vérifie qu'elle n'est pas déjà là
    public void ajouteFamilleCreee(Famille f) {
        if (!this.famillesCrees.contains(f)) {
            this.famillesCrees.add(f);
        }
    }

    // --- Getters (Lecture des infos) ---
    
    public Famille getFamilleOrigine() {
        return familleOrigine;
    }

    // Je combine prénom et nom pour l'affichage
    public String getNomComplet() {
        return prenom.getValeur() + " " + nom.getValeur();
    }
    
    // Utile pour vérifier la cohérence du sexe (Exceptions)
    public String getSexeValeur() {
        return sexe.getValeur(); 
    }
    
    // Pour trouver mes enfants, je dois regarder dans toutes les familles que j'ai créées
    public List<Individu> getEnfants() {
        List<Individu> tousLesEnfants = new ArrayList<>();
        for (Famille f : famillesCrees) {
            tousLesEnfants.addAll(f.getEnfants());
        }
        return tousLesEnfants;
    }
    
    // Pour trouver mes frères et soeurs (SIBLINGS)
    public List<Individu> getFreresSoeurs() {
        List<Individu> siblings = new ArrayList<>();
        // Je remonte à mes parents (familleOrigine)
        if (familleOrigine != null) {
            // Je prends tous les enfants de mes parents
            for (Individu enfant : familleOrigine.getEnfants()) {
                // Je m'exclus de la liste (je ne suis pas mon propre frère)
                if (!enfant.getId().equals(this.id)) {
                    siblings.add(enfant);
                }
            }
        }
        return siblings;
    }

    @Override
    public String toString() {
        // Si c'est un fantôme qui n'a jamais été défini, je le signale
        if (estFantome) return id + " [ERREUR : INDIVIDU MANQUANT]";
        return id + " : " + getNomComplet() + " (" + sexe.getValeur() + ")";
    }
}