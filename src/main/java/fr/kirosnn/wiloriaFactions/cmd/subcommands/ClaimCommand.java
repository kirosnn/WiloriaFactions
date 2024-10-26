package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ClaimCommand {
    private final WiloriaFactions plugin;

    public ClaimCommand(WiloriaFactions plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player) {
        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage("§cVous n'êtes pas dans une faction !");
            return;
        }

        if (!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage("§cSeul le leader peut claim un terrain !");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        FLocation location = new FLocation(chunk);

        if (plugin.getFactions().isChunkClaimed(location)) {
            player.sendMessage("§cCe chunk est déjà claim par une autre faction !");
            return;
        }

        faction.addClaim(location);
        plugin.getFactions().forceSave();
        player.sendMessage("§aChunk claimé avec succès pour votre faction !");
    }
}
