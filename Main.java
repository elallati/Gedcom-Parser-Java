package fr.univ.gedcom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        GedcomParser parser = new GedcomParser();
        
        // J'utilise BufferedReader car c'est mieux que Scanner pour gérer les entrées clavier
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("--- LOGICIEL GENEALOGIE ---");
        System.out.println("Merci de placer le fichier .ged a la racine du projet.");
        
        try {

            System.out.print("Nom du fichier (ex: begood.ged) : ");
            String saisie = reader.readLine();
            
            // Si l'utilisateur tape juste ENTRÉE, on prend begood.ged par défaut
            String fichierATester = (saisie == null || saisie.trim().isEmpty()) ? "begood.ged" : saisie.trim();
            
            // Lecture du fichier. Si erreur, on saute directement dans les catch en bas.
            parser.lireFichier(fichierATester); 
            
            System.out.println("Fichier " + fichierATester + " charge avec succes !");
            System.out.println("Commandes : INFO [Nom], CHILD [Nom], SIBLINGS [Nom], MARRIED [Nom1, Nom2], FAMC [Nom], EXIT");

            boolean running = true;
            
            // Boucle principale qui attend les commandes de l'utilisateur
            while (running) {
                System.out.print("\nVotre commande > ");
                
                String ligne = reader.readLine();
                if (ligne == null) break; // Arrêt si Ctrl+D
                
                ligne = ligne.trim();
                
                if (ligne.equals("EXIT")) {
                    running = false;
                    continue;
                }
                if (ligne.isEmpty()) continue;
                
                // Je sépare la commande (ex: INFO) et l'argument (ex: Jean)
                String[] parts = ligne.split(" ", 2);
                String cmd = parts[0].toUpperCase();
                String argument = (parts.length > 1) ? parts[1] : "";
                
                // Je cherche la personne concernée (sauf pour MARRIED qui en a 2)
                Individu cible = null;
                if (!argument.isEmpty() && !cmd.equals("MARRIED")) {
                    cible = trouverPersonne(parser, argument);
                }

                switch (cmd) {
                    case "INFO":
                        if (cible != null) System.out.println(cible);
                        else System.out.println("Personne introuvable.");
                        break;
                        
                    case "CHILD":
                        if (cible != null) {
                            List<Individu> enfants = cible.getEnfants();
                            if (enfants.isEmpty()) System.out.println(cible.getNomComplet() + " n'a pas d'enfants.");
                            else {
                                System.out.println("Enfants de " + cible.getNomComplet() + " :");
                                for (Individu enf : enfants) System.out.println("- " + enf.getNomComplet());
                            }
                        } else System.out.println("Personne introuvable.");
                        break;
                        
                    case "SIBLINGS":
                        if (cible != null) {
                            List<Individu> freres = cible.getFreresSoeurs();
                            if (freres.isEmpty()) System.out.println(cible.getNomComplet() + " n'a pas de freres/soeurs.");
                            else {
                                System.out.println("Freres/Soeurs de " + cible.getNomComplet() + " :");
                                for (Individu f : freres) System.out.println("- " + f.getNomComplet());
                            }
                        } else System.out.println("Personne introuvable.");
                        break;
                    
                    case "FAMC": // Commande demandée : afficher la famille d'origine
                        if (cible != null) {
                            Famille fam = cible.getFamilleOrigine();
                            if (fam != null) {
                                System.out.println("Famille d'origine de " + cible.getNomComplet() + " (ID: " + fam.getId() + ") :");
                                // J'affiche les parents s'ils existent
                                if (fam.getMari() != null) System.out.println("- Pere : " + fam.getMari().getNomComplet());
                                else System.out.println("- Pere : Inconnu");
                                
                                if (fam.getFemme() != null) System.out.println("- Mere : " + fam.getFemme().getNomComplet());
                                else System.out.println("- Mere : Inconnue");
                            } else {
                                System.out.println(cible.getNomComplet() + " n'a pas de famille d'origine connue.");
                            }
                        } else {
                            System.out.println("Personne introuvable.");
                        }
                        break;

                    case "MARRIED":
                        // Cas spécial : il faut parser deux noms séparés par une virgule
                        if (!argument.contains(",")) {
                            System.out.println("Erreur format. Tapez : MARRIED Nom1, Nom2");
                            break;
                        }
                        String[] noms = argument.split(",");
                        Individu p1 = trouverPersonne(parser, noms[0]);
                        Individu p2 = trouverPersonne(parser, noms[1]);

                        if (p1 == null || p2 == null) {
                            System.out.println("Impossible de trouver l'une des deux personnes.");
                        } else {
                            // Je vérifie s'ils sont mari et femme dans une des familles
                            boolean maries = false;
                            for (Famille f : parser.getFamilles()) {
                                Individu mari = f.getMari();
                                Individu femme = f.getFemme();
                                if (mari != null && femme != null) {
                                    if ((mari == p1 && femme == p2) || (mari == p2 && femme == p1)) {
                                        maries = true;
                                        break;
                                    }
                                }
                            }
                            if (maries) System.out.println("OUI, " + p1.getNomComplet() + " et " + p2.getNomComplet() + " sont maries.");
                            else System.out.println("NON, ils ne sont pas maries ensemble.");
                        }
                        break;
                        
                    default:
                        System.out.println("Commande inconnue.");
                }
            }

        // --- Gestion des Exceptions (du plus précis au plus général) ---
        
        } catch (IndividuInexistantException e) {
            // Si le parser a trouvé un fantôme
            System.err.println("\n[ALERTE COHERENCE] : " + e.getMessage());
            System.err.println("-> ID manquant : " + e.getIdManquant());
            
        } catch (SexeIncoherentException e) {
            // Si un père est une femme, etc.
            System.err.println("\n[ALERTE SEXE] : " + e.getMessage());
            
        } catch (FamilleVideException e) {
            // Si une famille ne contient personne
            System.err.println("\n[ALERTE NETTOYAGE] : " + e.getMessage());

        } catch (GedcomException e) {
            // Les autres erreurs du projet
            System.err.println("Erreur GEDCOM : " + e.getMessage());
            
        } catch (IOException e) {
            System.err.println("Erreur d'entree/sortie : " + e.getMessage());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Fin du programme.");
    }

    // Méthode pour chercher un individu dans la liste
    private static Individu trouverPersonne(GedcomParser parser, String nomCherche) {
        String recherche = nomCherche.trim().toLowerCase();
        for (Individu i : parser.getIndividus()) {
            if (!i.isFantome() && i.getNomComplet().toLowerCase().contains(recherche)) {
                return i;
            }
        }
        return null;
    }
}