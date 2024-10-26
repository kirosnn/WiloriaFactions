package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import fr.kirosnn.wiloriaFactions.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DemoteCommand {

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

        if (args.length < 2) {
            player.sendMessage("Usage : /f demote <joueur>");
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null || !faction.getMembers().contains(targetPlayer.getUniqueId())) {
            player.sendMessage("Le joueur spécifié n'est pas dans votre faction.");
            return;
        }

        // Vérification pour empêcher de se rétrograder soi-même
        if (targetPlayer.equals(player)) {
            player.sendMessage("Vous ne pouvez pas vous rétrograder vous-même.");
            return;
        }

        Role targetRole = faction.getRole(targetPlayer.getUniqueId());
        Role demoterRole = faction.getRole(player.getUniqueId());

        if (demoterRole.ordinal() <= targetRole.ordinal()) {
            player.sendMessage("Vous n'avez pas la permission de rétrograder ce joueur.");
            return;
        }

        boolean demoted = faction.demoteMember(targetPlayer.getUniqueId());
        if (!demoted) {
            player.sendMessage("Le joueur ne peut pas être rétrogradé davantage.");
            return;
        }

        factionsManager.forceSave();

        Role newRole = faction.getRole(targetPlayer.getUniqueId());
        String demotionMessage = targetPlayer.getName() + " a été rétrogradé au grade " + newRole.name() + " dans la faction " + faction.getName() + "!";

        // Message pour le serveur
        Bukkit.broadcastMessage(demotionMessage);

        // Message pour le joueur rétrogradé et le rétrogradeur
        targetPlayer.sendMessage("Vous avez été rétrogradé au grade " + newRole.name() + " par " + player.getName() + " !");
        player.sendMessage("Vous avez rétrogradé " + targetPlayer.getName() + " au grade " + newRole.name() + " !");
    }
}
