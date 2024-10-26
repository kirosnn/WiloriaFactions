/*
UnclaimCommand.java
 */

package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class UnclaimCommand {
    private final WiloriaFactions plugin;

    public UnclaimCommand(WiloriaFactions plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player) {
        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage("§cVous n'êtes pas dans une faction !");
            return;
        }

        if (!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage("§cSeul le leader peut unclaim un terrain !");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        FLocation location = new FLocation(chunk);

        if (!faction.hasClaim(location)) {
            player.sendMessage("§cCe chunk n'appartient pas à votre faction !");
            return;
        }

        faction.removeClaim(location);
        plugin.getFactions().forceSave();
        player.sendMessage("§aChunk unclaimé avec succès pour votre faction !");
    }
}
