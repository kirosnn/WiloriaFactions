/*
ClaimEnterListener.java gère les titles entre claims
 */

package fr.kirosnn.wiloriaFactions.listeners;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ClaimEnterListener implements Listener {

    private final Factions factions;
    private final WiloriaFactions plugin;

    public ClaimEnterListener(WiloriaFactions plugin) {
        this.plugin = plugin;
        this.factions = plugin.getFactions();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        FLocation fromLocation = new FLocation(event.getFrom());
        FLocation toLocation = new FLocation(event.getTo());

        if (fromLocation.equals(toLocation)) {
            return;
        }

        Faction fromFaction = factions.getFactionByLocation(fromLocation);
        Faction toFaction = factions.getFactionByLocation(toLocation);

        if (toFaction == fromFaction) {
            return;
        }

        displayFactionTitle(player, toFaction);
    }

    private void displayFactionTitle(Player player, Faction faction) {
        if (faction == null) {
            String title = ChatColor.DARK_GREEN + "Terre non conquise";
            String subtitle = ChatColor.GRAY + "Cette zone n'appartient à personne";
            player.sendTitle(title, subtitle, 10, 70, 20);
        } else {
            String titleColor = faction.getMembers().contains(player.getUniqueId()) ? ChatColor.GREEN.toString() : ChatColor.RED.toString();
            String title = titleColor + faction.getName();
            String subtitle = ChatColor.GRAY + faction.getDescription();
            player.sendTitle(title, subtitle, 10, 70, 20);
        }
    }
}
