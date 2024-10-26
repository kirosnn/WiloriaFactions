/*
FactionTabCompleter.java gère le tab-completion des commandes.
 */

package fr.kirosnn.wiloriaFactions.cmd.tabcompleter;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FactionTabCompleter implements TabCompleter {

    private final List<String> baseCommands = Arrays.asList(
            "create",
            "desc",
            "claim",
            "unclaim",
            "unclaimall",
            "invite",
            "kick",
            "promote",
            "demote",
            "sethome",
            "home",
            "map",
            "leave",
            "help"
    );

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            return getBaseCommands(args[0]);
        }

        return switch (args[0].toLowerCase()) {
            case "invite" -> getInvitablePlayers(player, args[1]);
            case "kick", "promote", "demote" -> getFactionMembers(player, args[1]);
            case "unclaim", "unclaimall" -> getConfirmation(args[1]);
            default -> new ArrayList<>();
        };
    }

    /**
     * Retourne les commandes de base filtrées selon le début de la saisie
     */
    private List<String> getBaseCommands(String start) {
        return baseCommands.stream()
                .filter(cmd -> cmd.toLowerCase().startsWith(start.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Retourne la liste des joueurs invitables (non membres de la faction)
     */
    private List<String> getInvitablePlayers(Player player, String start) {
        Faction faction = WiloriaFactions.getInstance().getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) return new ArrayList<>();

        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> !faction.getMembers().contains(p.getUniqueId()))
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(start.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Retourne la liste des membres de la faction
     */
    private List<String> getFactionMembers(Player player, String start) {
        Faction faction = WiloriaFactions.getInstance().getFactions().getFactionByPlayer(player.getUniqueId());
        if (faction == null) return new ArrayList<>();

        return faction.getMembers().stream()
                .map(uuid -> Bukkit.getPlayer(uuid))
                .filter(p -> p != null && p != player)
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(start.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Retourne la suggestion de confirmation si nécessaire
     */
    private List<String> getConfirmation(String start) {
        if ("confirm".startsWith(start.toLowerCase())) {
            return Arrays.asList("confirm");
        }
        return new ArrayList<>();
    }
}