package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import fr.kirosnn.wiloriaFactions.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromoteCommand {

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
            player.sendMessage("Usage : /f promote <joueur>");
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null || !faction.getMembers().contains(targetPlayer.getUniqueId())) {
            player.sendMessage("Le joueur spécifié n'est pas dans votre faction.");
            return;
        }

        // Vérification pour empêcher de se promouvoir soi-même
        if (targetPlayer.equals(player)) {
            player.sendMessage("Vous ne pouvez pas vous promouvoir vous-même.");
            return;
        }

        Role targetRole = faction.getRole(targetPlayer.getUniqueId());
        Role promoterRole = faction.getRole(player.getUniqueId());

        if (promoterRole.ordinal() <= targetRole.ordinal()) {
            player.sendMessage("Vous n'avez pas la permission de promouvoir ce joueur.");
            return;
        }

        boolean promoted = faction.promoteMember(targetPlayer.getUniqueId());
        if (!promoted) {
            player.sendMessage("Le joueur est déjà au rang le plus élevé.");
            return;
        }

        factionsManager.forceSave();

        Role newRole = faction.getRole(targetPlayer.getUniqueId());
        String promotionMessage = targetPlayer.getName() + " a été promu au grade " + newRole.name() + " dans la faction " + faction.getName() + "!";

        // Message pour le serveur
        Bukkit.broadcastMessage(promotionMessage);

        // Message pour le joueur promu et le promoteur
        targetPlayer.sendMessage("Vous avez été promu au grade " + newRole.name() + " par " + player.getName() + " !");
        player.sendMessage("Vous avez promu " + targetPlayer.getName() + " au grade " + newRole.name() + " !");
    }
}
