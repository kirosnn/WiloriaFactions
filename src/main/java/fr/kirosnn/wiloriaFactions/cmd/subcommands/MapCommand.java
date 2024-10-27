package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.claims.FLocation;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class MapCommand {

    private final Factions factions;

    public MapCommand(Factions factions) {
        this.factions = factions;
    }

    public void execute(Player player) {
        Chunk playerChunk = player.getLocation().getChunk();
        FLocation playerLocation = new FLocation(player.getWorld().getName(), playerChunk.getX(), playerChunk.getZ());
        Faction currentFaction = factions.getFactionByLocation(playerLocation);

        String claimOwner = (currentFaction != null) ? currentFaction.getName() : "§aTerre non conquise";
        int headerWidth = 24;
        int remainingWidth = Math.max(0, headerWidth - claimOwner.length() - 8);
        int sideWidth = remainingWidth / 2;

        StringBuilder headerText = new StringBuilder(ChatColor.YELLOW.toString());
        headerText.append("-".repeat(sideWidth)).append(" ");
        headerText.append(ChatColor.GREEN).append("(").append(playerChunk.getX()).append(", ").append(playerChunk.getZ()).append(") ");
        headerText.append(ChatColor.YELLOW).append("-".repeat(sideWidth));

        TextComponent header = new TextComponent(headerText.toString());
        TextComponent claimInfo = new TextComponent((currentFaction != null) ? ChatColor.GREEN + claimOwner : ChatColor.GRAY + claimOwner);

        if (currentFaction != null) {
            claimInfo.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                    net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.GRAY + "Propriétaire: " + claimOwner).create()
            ));
        }

        header.addExtra(claimInfo);

        ComponentBuilder mapBuilder = new ComponentBuilder();

        for (int dz = 4; dz >= -4; dz--) {
            for (int dx = -12; dx <= 12; dx++) {
                FLocation location = new FLocation(player.getWorld().getName(), playerChunk.getX() + dx, playerChunk.getZ() + dz);
                Faction faction = factions.getFactionByLocation(location);
                TextComponent factionSymbol;

                if (dx == 0 && dz == 0) {
                    factionSymbol = new TextComponent(ChatColor.YELLOW + "+");
                } else if (faction == null) {
                    factionSymbol = new TextComponent(ChatColor.DARK_GRAY + "-");
                    factionSymbol.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                            net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("§aTerre non conquise").create()
                    ));
                } else if (faction.getMembers().contains(player.getUniqueId())) {
                    factionSymbol = new TextComponent(ChatColor.GREEN + "+");
                    factionSymbol.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                            net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(ChatColor.GRAY + "Faction: " + faction.getName()).create()
                    ));
                } else {
                    factionSymbol = new TextComponent(ChatColor.RED + "+");
                    factionSymbol.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                            net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(ChatColor.GRAY + "Faction: " + faction.getName()).create()
                    ));
                }

                mapBuilder.append(factionSymbol);
            }
            mapBuilder.append("\n");
        }

        player.spigot().sendMessage(new ComponentBuilder(header).append("\n").append(mapBuilder.create()).create());
    }
}
