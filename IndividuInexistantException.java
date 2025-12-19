package fr.univ.gedcom;

public class IndividuInexistantException extends GedcomException {
    private static final long serialVersionUID = 1L;

    // On stocke l'ID qui pose problème pour pouvoir l'afficher
    private String idManquant;

    public IndividuInexistantException(String id) {
        // Le message précis pour l'utilisateur
        super("Incohérence détectée : L'individu " + id + " est cité dans une famille mais n'existe pas.");
        this.idManquant = id;
    }
    
    public String getIdManquant() {
        return idManquant;
    }
}