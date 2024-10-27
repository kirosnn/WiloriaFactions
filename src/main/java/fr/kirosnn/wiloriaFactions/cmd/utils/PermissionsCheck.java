package fr.kirosnn.wiloriaFactions.cmd.utils;

import fr.kirosnn.wiloriaFactions.Role;
import org.bukkit.ChatColor;

public class PermissionsCheck {

    public static boolean canModifyPermissions(Role role) {
        return role == Role.LEADER || role == Role.COLEADER;
    }

    public static boolean isPermissionsGui(String title) {
        return ChatColor.stripColor(title).equalsIgnoreCase("Permissions Faction");
    }
}
