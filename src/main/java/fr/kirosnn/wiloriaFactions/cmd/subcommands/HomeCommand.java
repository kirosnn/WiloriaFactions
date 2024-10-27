package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class HomeCommand {

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return;
        }

        Player player = (Player) sender;
        Factions factionsManager = WiloriaFactions.getInstance().getFactions();
        Faction faction = factionsManager.getFactionByPlayer(player.getUniqueId());

        if (faction == null) {
            player.sendMessage("Vous n'appartenez à aucune faction.");
            return;
        }

        Location homeLocation = faction.getHome();
        if (homeLocation == null) {
            player.sendMessage("Votre faction n'a pas encore défini de home.");
            return;
        }

        player.sendMessage("Téléportation au home de la faction dans 5 secondes... Ne bougez pas!");

        Bukkit.getScheduler().runTaskLater(WiloriaFactions.getInstance(), () -> {
            if (player.isOnline()) {
                player.teleport(homeLocation);
                player.sendMessage("Vous avez été téléporté au home de votre faction.");
            }
        }, 100L);
    }
}
