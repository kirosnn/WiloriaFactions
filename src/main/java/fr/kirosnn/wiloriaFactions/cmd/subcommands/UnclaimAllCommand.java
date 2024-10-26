/*
UnclaimAllCommand.java
 */

package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.entity.Player;

public class UnclaimAllCommand {
    private final WiloriaFactions plugin;

    public UnclaimAllCommand(WiloriaFactions plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player) {
        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage("§cVous n'êtes pas dans une faction !");
            return;
        }

        if (!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage("§cSeul le leader peut unclaim tous les terrains !");
            return;
        }

        faction.getClaims().clear();
        plugin.getFactions().forceSave();
        player.sendMessage("§aTous les chunks ont été unclaimés pour votre faction !");
    }
}
