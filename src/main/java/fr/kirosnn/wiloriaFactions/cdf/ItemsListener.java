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

    private final Factions factionsManager;

    public ItemsListener(Factions factionsManager) {
        this.factionsManager = factionsManager;
    }

    @EventHandler
    public void onFactionHeartPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        CustomStack placedItem = CustomStack.byItemStack(event.getItemInHand());

        if (placedItem != null && placedItem.getId().equals("_iainternal:1")) {
            event.setCancelled(false);

            Location location = event.getBlock().getLocation();

            location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1);
            location.getWorld().spawnParticle(Particle.FIREWORK, location, 50, 1, 1, 1, 0.1);

            double radius = 5.0;
            for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
                if (entity instanceof Player nearbyPlayer && !nearbyPlayer.equals(player)) {
                    Vector pushDirection = nearbyPlayer.getLocation().toVector().subtract(location.toVector()).normalize().multiply(1.5);
                    nearbyPlayer.setVelocity(pushDirection);
                }
            }

            // Envoi d'un message global
            Faction faction = factionsManager.getFactionByPlayer(player.getUniqueId());
            if (faction != null) {
                String factionName = faction.getName();
                String broadcastMessage = ChatColor.RED + player.getName() + " de la faction " + factionName + " a placé le Coeur de Faction !";
                Bukkit.broadcastMessage(broadcastMessage);

                // Message de félicitations au joueur
                player.sendMessage(ChatColor.GREEN + "Félicitations ! Vous avez placé le Coeur de Faction pour votre faction.");
            }
        }
    }
}
