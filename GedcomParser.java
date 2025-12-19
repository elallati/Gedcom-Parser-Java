package fr.univ.gedcom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
import java.util.List;

public class GedcomParser {

    // Je stocke mes données dans des Listes (ArrayList) au lieu des Maps
    // car pendant les tp on utilise des vecteurs/collections simples.
    private List<Individu> repertoireIndividus;
    private List<Famille> repertoireFamilles;

    public GedcomParser() {
        this.repertoireIndividus = new ArrayList<>();
        this.repertoireFamilles = new ArrayList<>();
    }

    // Petite méthode pour chercher quelqu'un dans la liste avec une boucle for.
    private Individu trouverIndividuParId(String id) {
        for (Individu i : repertoireIndividus) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null; // Si j'ai rien trouvé
    }

    /**
     * C'est ici que je gère le problème des références manquantes.
     * Si on me demande un ID que je connais pas encore, je crée un "Fantôme".
     */
    private Individu getOuCreerIndividu(String id) {
        Individu existant = trouverIndividuParId(id);
        
        if (existant != null) {
            return existant; // Super, il existe déjà
        } else {
            // Je ne le connais pas. Je crée une coquille vide.
            Individu fantome = new Individu(id);
            fantome.setFantome(true); // Je mets un marqueur pour dire "Attention c'est un fantôme"
            repertoireIndividus.add(fantome); // Je le sauvegarde direct
            return fantome;
        }
    }

    public void lireFichier(String chemin) throws GedcomException {
        System.out.println("Lecture du fichier " + chemin + "...");

        // J'utilise BufferedReader pour lire le fichier proprement ligne par ligne
        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            
            // Cette variable est cool : c'est du Polymorphisme.
            // Elle peut contenir soit un Individu, soit une Famille selon ce qu'on lit.
            Entite elementCourant = null;

            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim(); // J'enlève les espaces inutiles
                if (ligne.isEmpty()) continue;

                // Je coupe la ligne : 0 @I1@ INDI
                String[] parties = ligne.split(" ", 3);
                String tag = (parties.length > 1) ? parties[1] : "";
                String valeur = (parties.length > 2) ? parties[2] : "";

                // --- Si la ligne commence par un ID (Niveau 0) ---
                if (parties.length > 2 && parties[1].startsWith("@")) {
                    String id = parties[1];
                    tag = parties[2];

                    if (tag.equals("INDI")) {
                        // Je récupère l'individu (ou je le crée s'il n'existait pas)
                        Individu ind = getOuCreerIndividu(id);
                        ind.setFantome(false); // Maintenant je le définis, donc ce n'est plus un fantôme
                        elementCourant = ind;
                        
                    } else if (tag.equals("FAM")) {
                        // Nouvelle famille
                        Famille fam = new Famille(id);
                        repertoireFamilles.add(fam);
                        elementCourant = fam;
                    }
                } 
                // --- Si c'est une info (Niveau 1) ---
                // Si j'ai déjà commencé à remplir quelque chose (elementCourant n'est pas null)
                else if (elementCourant != null) {
                    if (elementCourant instanceof Individu) {
                        traiterIndividu((Individu) elementCourant, tag, valeur);
                    } else if (elementCourant instanceof Famille) {
                        traiterFamille((Famille) elementCourant, tag, valeur);
                    }
                }
                // Si c'est un tag inconnu (HEAD, BIRT...), je ne fais rien, le programme continue.
            }
            
            // Une fois fini, je vérifie si tout est logique
            verifierCoherence();

        } catch (IOException e) {
            throw new GedcomException("Problème avec le fichier : " + e.getMessage());
        }
    }

    private void traiterIndividu(Individu ind, String tag, String val) {
        switch (tag) {
            case "NAME":
                // Les noms sont du type "Prénom /Nom/"
                String[] noms = val.split("/");
                ind.setPrenom(noms[0].trim());
                if (noms.length > 1) ind.setNom(noms[1].trim());
                break;
            case "SEX":
                ind.setSexe(val);
                break;
        }
    }

    private void traiterFamille(Famille fam, String tag, String val) {
        switch (tag) {
            case "HUSB":
            case "WIFE":
                // Je récupère le parent
                Individu parent = getOuCreerIndividu(val);
                
                // Je l'assigne au bon rôle
                if (tag.equals("HUSB")) fam.setMari(parent);
                else fam.setFemme(parent);
                
                // Important : Je crée le lien inverse (le parent connait aussi la famille)
                parent.ajouteFamilleCreee(fam); 
                break;
                
            case "CHIL":
                Individu enfant = getOuCreerIndividu(val);
                fam.ajouteEnfant(enfant);
                enfant.setFamilleOrigine(fam); // Lien réciproque ici aussi
                break;
        }
    }

    private void verifierCoherence() throws GedcomException {
        // 1. Je vérifie s'il reste des Fantômes (des gens cités mais jamais définis)
        for (Individu i : repertoireIndividus) {
            if (i.isFantome()) {
                throw new IndividuInexistantException(i.getId());
            }
        }

        // 2. Je vérifie la logique dans les familles
        for (Famille f : repertoireFamilles) {
            // Si la famille est vide, ça sert à rien de la garder
            if (f.getMari() == null && f.getFemme() == null && f.getEnfants().isEmpty()) {
                throw new FamilleVideException(f.getId());
            }

            // Je vérifie que le père n'est pas une femme
            if (f.getMari() != null && f.getMari().getSexeValeur().equals("F")) {
                 throw new SexeIncoherentException(f.getMari().getId(), "PERE", "FEMININ");
            }
            // Je vérifie que la mère n'est pas un homme
            if (f.getFemme() != null && f.getFemme().getSexeValeur().equals("M")) {
                 throw new SexeIncoherentException(f.getFemme().getId(), "MERE", "MASCULIN");
            }
        }
    }

    // Getters simples pour récupérer les listes
    public List<Individu> getIndividus() { return repertoireIndividus; }
    public List<Famille> getFamilles() { return repertoireFamilles; }
}