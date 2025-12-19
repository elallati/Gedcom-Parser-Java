package fr.univ.gedcom;

/**
 * Exception levée quand la biologie n'est pas respectée (Papa femme / Maman homme).
 */
public class SexeIncoherentException extends GedcomException {
    private static final long serialVersionUID = 1L;

    public SexeIncoherentException(String id, String role, String sexeTrouve) {
        // Je prépare un message précis : "L'individu X est PERE mais son sexe est FEMININ"
        super("Incoherence Sexe : L'individu " + id + " est " + role + " mais son sexe est " + sexeTrouve);
    }
}