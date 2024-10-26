/*
DescriptionCommand.java
 */

package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.entity.Player;

public class DescriptionCommand {
    private final WiloriaFactions plugin;

    public DescriptionCommand(WiloriaFactions plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUtilisation : /f desc <description>");
            return;
        }

        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage("§cVous n'êtes pas dans une faction !");
            return;
        }

        if (!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage("§cSeul le leader peut modifier la description !");
            return;
        }

        String description = String.join(" ", args).substring(args[0].length() + 1);
        faction.setDescription(description);
        plugin.getFactions().forceSave();
        player.sendMessage("§aDescription de la faction mise à jour !");
    }
}
