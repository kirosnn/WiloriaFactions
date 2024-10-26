package fr.kirosnn.wiloriaFactions.claims;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClaimManager {
    private final WiloriaFactions plugin;

    public ClaimManager(WiloriaFactions plugin) {
        this.plugin = plugin;
    }

    public boolean canClaim(Player player, FLocation loc) {
        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) return false;

        if (isChunkClaimed(loc)) {
            player.sendMessage("§cCe chunk est déjà claim !");
            return false;
        }

        if (faction.getFirstClaim() == null) {
            return true;
        }

        return faction.getClaims().stream()
                .anyMatch(claim -> claim.getAdjacentLocations().contains(loc));
    }

    public boolean claim(Player player, FLocation loc) {
        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null || !canClaim(player, loc)) return false;

        faction.addClaim(loc);
        plugin.getFactions().forceSave();

        // Si c'est le premier claim
        if (faction.getFirstClaim() == null) {
            faction.setFirstClaim(loc);
        }

        player.sendMessage("§aChunk claim avec succès !");

        for (UUID memberId : faction.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null && member != player) {
                member.sendMessage("§6" + player.getName() + " §aa claim un nouveau chunk en §6" +
                        loc.getX() + ", " + loc.getZ());
            }
        }

        return true;
    }

    public boolean unclaim(Player player, FLocation loc) {
        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) return false;

        if (!faction.hasClaim(loc)) {
            player.sendMessage("§cCe chunk n'appartient pas à votre faction !");
            return false;
        }

        if (loc.equals(faction.getFirstClaim())) {
            if (!faction.isPendingFirstClaimUnclaim()) {
                player.sendMessage("§cAttention ! C'est le premier claim de votre faction !");
                player.sendMessage("§cSi vous le supprimez, tous les claims connectés seront perdus.");
                player.sendMessage("§cTapez à nouveau la commande pour confirmer.");
                faction.setPendingFirstClaimUnclaim(true);
                return false;
            }
            faction.setPendingFirstClaimUnclaim(false);
            faction.removeFirstClaim();
            player.sendMessage("§cVous avez supprimé le premier claim de votre faction !");
            player.sendMessage("§cTous les claims connectés ont été supprimés !");
        } else {
            faction.removeClaim(loc);
            player.sendMessage("§aChunk unclaim avec succès !");
        }

        plugin.getFactions().forceSave();
        return true;
    }

    private boolean isChunkClaimed(FLocation loc) {
        return plugin.getFactions().getAllFactions().stream()
                .anyMatch(f -> f.hasClaim(loc));
    }
}