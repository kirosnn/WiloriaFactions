package fr.kirosnn.wiloriaFactions.listeners;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class FactionFriendlyFireListener implements Listener {
    private final WiloriaFactions plugin;

    public FactionFriendlyFireListener(WiloriaFactions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        UUID damagerUUID = damager.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        Faction damagerFaction = plugin.getFactions().getFactionByPlayer(damagerUUID);
        Faction targetFaction = plugin.getFactions().getFactionByPlayer(targetUUID);

        if (damagerFaction != null && damagerFaction.equals(targetFaction)) {
            damager.sendMessage(ChatColor.RED + "Vous ne pouvez pas attaquer les membres de votre propre faction !");
            event.setCancelled(true);
        }
    }
}
