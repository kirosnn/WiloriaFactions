package fr.kirosnn.wiloriaFactions.cdf;

import dev.lone.itemsadder.api.CustomStack;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class ItemsListener implements Listener {

    private final Factions factions;

    public ItemsListener(Factions factions) {
        this.factions = factions;
    }

    @EventHandler
    public void onFactionHeartPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        CustomStack placedItem = CustomStack.byItemStack(event.getItemInHand());

        // Vérifie si l'item placé est le Coeur de Faction
        if (!isFactionHeartItem(placedItem)) {
            return;
        }

        // Vérification du statut de la faction du joueur
        Faction faction = factions.getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "Erreur : Vous n'appartenez à aucune faction !");
            return;
        }

        // Confirme le placement et exécute les effets visuels et le message
        event.setCancelled(false);  // Peut-être inutile, vérifiez selon les besoins
        triggerPlacementEffects(event.getBlock().getLocation(), player);
        announceFactionHeartPlacement(player, faction);
    }

    // Vérifie que l'item placé est bien le coeur de faction
    private boolean isFactionHeartItem(CustomStack item) {
        return item != null && item.getId().equals("_iainternal:1");
    }

    // Applique les effets visuels et repousse les joueurs proches
    private void triggerPlacementEffects(Location location, Player player) {
        location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1);
        location.getWorld().spawnParticle(Particle.FIREWORK, location, 50, 1, 1, 1, 0.1);
        repelNearbyPlayers(location, player, 5.0);
    }

    // Repousse les joueurs proches autour de la position donnée
    private void repelNearbyPlayers(Location location, Player placer, double radius) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (entity instanceof Player nearbyPlayer && !nearbyPlayer.equals(placer)) {
                Vector pushDirection = nearbyPlayer.getLocation().toVector().subtract(location.toVector()).normalize().multiply(1.5);
                nearbyPlayer.setVelocity(pushDirection);
                nearbyPlayer.sendMessage(ChatColor.RED + "Vous avez été repoussé par le Coeur de Faction de " + placer.getName());
            }
        }
    }

    // Annonce le placement du Coeur de Faction
    private void announceFactionHeartPlacement(Player player, Faction faction) {
        String factionName = faction.getName();
        String broadcastMessage = ChatColor.RED + player.getName() + " de la faction " + factionName + " a placé le Coeur de Faction !";

        // Annonce globale
        Bukkit.broadcastMessage(broadcastMessage);

        // Message de confirmation pour le joueur
        player.sendMessage(ChatColor.GREEN + "Félicitations ! Vous avez placé le Coeur de Faction pour votre faction.");
    }
}
