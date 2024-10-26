/*
HelpCommand.java
 */

package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand {
    private final List<String> helpPages;

    public HelpCommand() {
        helpPages = new ArrayList<>();
        helpPages.add("§6=== Commandes de Faction - Page 1 ===");
        helpPages.add("§6/f create <nom> : Crée une faction");
        helpPages.add("§6/f desc <description> : Modifie la description de la faction");
        helpPages.add("§6/f claim : Permet de claim un chunk pour votre faction");
        helpPages.add("§6/f unclaim [confirm] : Permet de unclaim un chunk pour votre faction");
        helpPages.add("§6/f unclaimall : Permet de unclaim tous les chunks pour votre faction");
        helpPages.add("§6/f core : Obtenez le Coeur de Faction (leader uniquement)");

        helpPages.add("§6=== Commandes de Faction - Page 2 ===");
        helpPages.add("§6/f list : Affiche la liste des factions");
    }

    public void execute(Player player, String[] args) {
        int page = 1;

        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cNuméro de page invalide !");
                return;
            }
        }

        int maxPages = (int) Math.ceil(helpPages.size() / 5.0);
        if (page < 1 || page > maxPages) {
            player.sendMessage("§cNuméro de page invalide ! Il y a " + maxPages + " pages.");
            return;
        }

        int start = (page - 1) * 5;
        int end = Math.min(start + 5, helpPages.size());

        for (int i = start; i < end; i++) {
            player.sendMessage(helpPages.get(i));
        }
    }
}
