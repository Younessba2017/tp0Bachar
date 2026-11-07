package ma.emsi.bachar.tp0bachar.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.util.Locale;

/**
 * Classe de service pour les modificateurs de question.
 * Un modificateur de question prend en entrée une question et retourne
 * la question modifiée.
 */
@Dependent // Portée CDI pour rendre la classe injectable par CDI dans les autres classes.
// Portée Dependent : l'instance sera supprimée quand la classe qui l'a injectée sera supprimée.
public class GenerateurReponse implements Serializable { // Serializable car CDI peut mettre l'instance en mémoire secondaire.
    private boolean roleSystemeAffiche = false;

    /**
     * Analyse des logs : chaque ligne commence par INFO, WARNING ou ERROR.
     * Les lignes vides sont ignorées et les autres lignes sont comptées séparément.
     */
    public String genererReponse(String question, String roleSysteme) {
        int infos = 0;
        int avertissements = 0;
        int erreurs = 0;
        int autres = 0;

        for (String ligne : question.split("\\R")) {
            if (ligne.isBlank()) {
                continue;
            }

            String niveau = ligne.strip()
                    .split("\\s+", 2)[0]
                    .toUpperCase(Locale.ROOT);

            switch (niveau) {
                case "INFO" -> infos++;
                case "WARNING" -> avertissements++;
                case "ERROR" -> erreurs++;
                default -> autres++;
            }
        }

        String bilan;
        if (erreurs > 0) {
            bilan = "Des erreurs sont à examiner.";
        } else if (avertissements > 0) {
            bilan = "Des avertissements sont à examiner.";
        } else if (infos > 0) {
            bilan = "Aucune erreur ni avertissement parmi les lignes reconnues.";
        } else {
            bilan = "Aucun niveau de log reconnu.";
        }

        String resultat = "||";
        if (!this.roleSystemeAffiche && roleSysteme != null) {
            resultat += roleSysteme.toUpperCase(Locale.FRENCH) + "\n";
            this.roleSystemeAffiche = true;
        }

        resultat += "Analyse des logs\n"
                + "INFO : " + infos + "\n"
                + "WARNING : " + avertissements + "\n"
                + "ERROR : " + erreurs + "\n"
                + "Lignes non reconnues : " + autres + "\n"
                + "Bilan : " + bilan + "||";

        return resultat;
    }
}