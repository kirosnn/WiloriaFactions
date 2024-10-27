package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.Role;
import fr.kirosnn.wiloriaFactions.cmd.utils.PermissionsCheck;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PermsCommand implements Listener {

    private final Factions factions;

    public PermsCommand(Factions factions) {
        this.factions = factions;
    }

    public void execute(Player player) {
        Faction faction = factions.getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        Role role = faction.getRole(player.getUniqueId());
        if (!PermissionsCheck.canModifyPermissions(role)) {
            player.sendMessage(ChatColor.RED + "You do not have permission to access this.");
            return;
        }

        Inventory permsGui = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Permissions Faction");
        if (role == Role.LEADER) {
            setupGuiForLeader(permsGui);
        } else if (role == Role.COLEADER) {
            setupGuiForColeader(permsGui);
        }

        player.openInventory(permsGui);
    }

    private void setupGuiForLeader(Inventory gui) {
        gui.setItem(10, createPermissionItem(Material.WOODEN_SWORD, ChatColor.GOLD + "Modify Recruit Permissions"));
        gui.setItem(12, createPermissionItem(Material.GOLDEN_SWORD, ChatColor.GOLD + "Modify Member Permissions"));
        gui.setItem(14, createPermissionItem(Material.DIAMOND_SWORD, ChatColor.GOLD + "Modify Moderator Permissions"));
        gui.setItem(16, createPermissionItem(Material.NETHERITE_SWORD, ChatColor.GOLD + "Modify Coleader Permissions"));
    }

    private void setupGuiForColeader(Inventory gui) {
        gui.setItem(10, createPermissionItem(Material.WOODEN_SWORD, ChatColor.GOLD + "Modify Recruit Permissions"));
        gui.setItem(12, createPermissionItem(Material.GOLDEN_SWORD, ChatColor.GOLD + "Modify Member Permissions"));
        gui.setItem(14, createPermissionItem(Material.DIAMOND_SWORD, ChatColor.GOLD + "Modify Moderator Permissions"));
        gui.setItem(16, createRestrictedItem(Material.BARRIER, ChatColor.RED + "Cannot modify Coleader Permissions"));
    }

    private ItemStack createPermissionItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createRestrictedItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!PermissionsCheck.isPermissionsGui(event.getView().getTitle())) return;

        event.setCancelled(true);  // Prevent items from being moved in the GUI
        Player player = (Player) event.getWhoClicked();
        // Additional click handling logic can go here
    }
}
