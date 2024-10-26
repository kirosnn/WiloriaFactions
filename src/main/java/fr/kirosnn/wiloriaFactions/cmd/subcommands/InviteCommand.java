package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand {
    private final WiloriaFactions plugin;
    private final Factions factions;

    public InviteCommand(WiloriaFactions plugin) {
        this.plugin = plugin;
        this.factions = plugin.getFactions();
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Seuls les joueurs peuvent utiliser cette commande.");
            return;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /f invite <joueur>");
            return;
        }

        Faction faction = factions.getFactionByPlayer(player.getUniqueId());
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "Vous n'êtes pas dans une faction.");
            return;
        }

        if (!faction.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Seul le leader de la faction peut inviter des joueurs.");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Joueur introuvable.");
            return;
        }

        factions.addInvitation(faction.getName(), target.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Vous avez invité " + target.getName() + " à rejoindre votre faction.");
        target.sendMessage(ChatColor.GOLD + "Vous avez été invité à rejoindre la faction " + faction.getName() + " par " + player.getName() + ". Utilisez /f join " + faction.getName() + " pour accepter.");
    }
}
