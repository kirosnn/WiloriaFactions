package fr.kirosnn.wiloriaFactions.listeners;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class FactionProtectionListener implements Listener {
    private final WiloriaFactions plugin;

    public FactionProtectionListener(WiloriaFactions plugin) {
        this.plugin = plugin;
    }

    private boolean canInteract(Player player, FLocation location) {
        Faction faction = plugin.getFactions().getFactionByLocation(location);
        return faction == null || faction.getMembers().contains(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        FLocation location = new FLocation(player.getLocation().getChunk());

        if (!canInteract(player, location)) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas détruire des blocs dans un territoire revendiqué par une autre faction !");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        FLocation location = new FLocation(player.getLocation().getChunk());

        if (!canInteract(player, location)) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas poser des blocs dans un territoire revendiqué par une autre faction !");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) return;

        FLocation location = new FLocation(block.getChunk());

        if (!canInteract(player, location)) {
            Material type = block.getType();

            if (type == Material.OAK_DOOR || type == Material.IRON_DOOR || type == Material.OAK_TRAPDOOR ||
                    type == Material.IRON_TRAPDOOR || type == Material.LEVER || type == Material.STONE_BUTTON ||
                    type == Material.OAK_BUTTON || type == Material.ACACIA_SIGN || type == Material.BIRCH_SIGN ||
                    type == Material.SPRUCE_SIGN || type == Material.JUNGLE_SIGN || type == Material.DARK_OAK_SIGN ||
                    type == Material.CRIMSON_SIGN || type == Material.WARPED_SIGN || type == Material.OAK_SIGN) {

                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas interagir avec des objets dans un territoire revendiqué par une autre faction !");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        FLocation location = new FLocation(player.getLocation().getChunk());

        List<String> restrictedCommands = Arrays.asList("/sethome", "/home");

        String command = event.getMessage().split(" ")[0].toLowerCase();
        if (restrictedCommands.contains(command) && !canInteract(player, location)) {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas utiliser cette commande dans un territoire revendiqué par une autre faction !");
            event.setCancelled(true);
        }
    }
}
