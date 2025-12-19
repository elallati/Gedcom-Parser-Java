package fr.univ.gedcom;

/**
 * Classe générale pour toutes les erreurs de mon projet.
 * Elle hérite de Exception, comme ça je peux faire des try/catch avec.
 */
public class GedcomException extends Exception {
    
    // Eclipse me demande de mettre ça parce que les Exceptions sont "Serialisables".
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de base.
     */
    public GedcomException(String message) {
        // J'envoie le message à la classe parent (Exception) de Java.
        // Comme ça, quand je ferai e.getMessage() dans le Main, Java saura quoi afficher.
        super(message);
    }
}