package fr.univ.gedcom;

import java.io.Serializable;

/**
 * Classe mère pour tout le monde (Individus et Familles).
 * C'est une classe abstraite car on ne crée jamais juste une "Entité".
 */
public abstract class Entite implements Serializable {
    // Le truc pour la sérialisation (sauvegarde) demandé dans le sujet
    private static final long serialVersionUID = 1L;
    
    // Je mets l'ID ici en protected pour que les classes filles (Individu, Famille)
    // puissent l'utiliser direct sans passer par des get/set compliqués.
    protected String id;

    public Entite(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}