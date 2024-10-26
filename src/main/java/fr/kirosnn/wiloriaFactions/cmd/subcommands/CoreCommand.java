/*
CoreCommand.java
 */

package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.cdf.Items;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CoreCommand {
    private final WiloriaFactions plugin;
    private final Items items;

    public CoreCommand(WiloriaFactions plugin, Items items) {
        this.plugin = plugin;
        this.items = items;
    }

    public void execute(Player player) {
        Faction faction = plugin.getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage("§cVous n'êtes pas dans une faction !");
            return;
        }

        if (!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage("§cSeul le leader de la faction peut obtenir cet item !");
            return;
        }

        if (faction.hasReceivedCore()) {
            player.sendMessage("§cVous avez déjà reçu le Coeur de Faction pour votre faction !");
            return;
        }

        ItemStack factionHeart = items.createFactionHeartItem();
        player.getInventory().addItem(factionHeart);
        faction.setHasReceivedCore(true);
        plugin.getFactions().forceSave();
        player.sendMessage("§aVous avez reçu le Coeur de Faction !");
    }
}
