/*
FactionChatListener.java
 */

package fr.kirosnn.wiloriaFactions.listeners;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class FactionChatListener implements Listener {

    private final WiloriaFactions plugin;
    private final Factions factions;

    public FactionChatListener(WiloriaFactions plugin) {
        this.plugin = plugin;
        this.factions = plugin.getFactions();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.startsWith("*")) {
            event.setCancelled(true);

            Faction faction = factions.getFactionByPlayer(player.getUniqueId());
            if (faction == null) {
                player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une faction.");
                return;
            }

            String factionMessage = "§7§l(§e§lFaction§7§l) " + ChatColor.YELLOW + player.getName() + " §r: " + message.substring(1).trim();
            for (UUID memberId : faction.getMembers()) {
                Player factionMember = plugin.getServer().getPlayer(memberId);
                if (factionMember != null && factionMember.isOnline()) {
                    factionMember.sendMessage(factionMessage);
                }
            }
        }
    }
}
