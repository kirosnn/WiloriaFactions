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

        // Vérifie si le joueur a changé de chunk
        FLocation fromLocation = new FLocation(event.getFrom());
        FLocation toLocation = new FLocation(event.getTo());

        if (fromLocation.equals(toLocation)) {
            return; // Ne rien faire si le joueur reste dans le même claim
        }

        // Récupère les factions associées aux claims d'origine et de destination
        Faction fromFaction = factions.getFactionByLocation(fromLocation);
        Faction toFaction = factions.getFactionByLocation(toLocation);

        // Ne rien faire si la faction est la même que celle du claim précédent
        if (toFaction == fromFaction) {
            return;
        }

        // Affichage du titre et du sous-titre
        displayFactionTitle(player, toFaction);
    }

    private void displayFactionTitle(Player player, Faction faction) {
        if (faction == null) {
            return; // Ne rien afficher si le claim n'appartient à aucune faction
        }

        String titleColor = faction.getMembers().contains(player.getUniqueId()) ? ChatColor.GREEN.toString() : ChatColor.RED.toString();
        String title = titleColor + faction.getName();
        String subtitle = ChatColor.GRAY + faction.getDescription();

        player.sendTitle(title, subtitle, 10, 70, 20); // Durée : fadeIn 10 ticks, stay 70 ticks, fadeOut 20 ticks
    }
}
