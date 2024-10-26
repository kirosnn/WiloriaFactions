/*
CreateFactionCommand.java
 */

package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CreateFactionCommand {
    private final Factions factions;

    public CreateFactionCommand(Factions factions) {
        this.factions = factions;
    }

    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("§cUtilisation : /f create <nom>");
            return;
        }

        String factionName = args[1];
        if (factions.getFactionByPlayer(player.getUniqueId()) != null) {
            player.sendMessage("§cVous êtes déjà dans une faction !");
            return;
        }

        if (factions.createFaction(factionName, player)) {
            player.sendMessage("§aVous avez créé la faction " + factionName + " !");
            String broadcastMessage = "§6" + player.getName() + " §aa créé la faction §6" + factionName + "§a !";
            Bukkit.broadcastMessage(broadcastMessage);
        } else {
            player.sendMessage("§cLe nom de faction est invalide ou déjà pris !");
        }
    }
}
