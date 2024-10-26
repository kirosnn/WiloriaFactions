/*
Items.java permet de créer le Coeur de Faction dans le plugin depuis ItemsAdder.
 */



package fr.kirosnn.wiloriaFactions.cdf;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Items {

    private final String ITEM_ID = "_iainternal:1";
    private final String DISPLAY_NAME = ChatColor.RED + "Coeur de Faction";
    private final Material MATERIAL = Material.PAPER;

    public ItemStack createFactionHeartItem() {
        ItemStack itemStack = new ItemStack(MATERIAL);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(DISPLAY_NAME);
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Cet item est le coeur de votre faction.",
                    ChatColor.GRAY + "Utilisez-le pour revendiquer votre territoire."
            ));
            itemStack.setItemMeta(meta);
        }

        CustomStack customStack = CustomStack.getInstance(ITEM_ID);
        if (customStack != null) {
            itemStack = customStack.getItemStack();
        }

        return itemStack;
    }
}
