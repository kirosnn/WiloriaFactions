package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import fr.kirosnn.wiloriaFactions.Role;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class SetHomeCommand {

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

        if (faction.getRole(player.getUniqueId()).ordinal() < Role.LEADER.ordinal()) {
            player.sendMessage("Vous n'avez pas la permission de définir le home de la faction.");
            return;
        }

        Location playerLocation = player.getLocation();
        FLocation location = new FLocation(playerLocation.getChunk());

        if (!faction.hasClaim(location)) {
            player.sendMessage("Vous devez être dans un claim de votre faction pour définir le home.");
            return;
        }

        faction.setHome(playerLocation);
        factionsManager.forceSave();
        player.sendMessage("Le home de la faction a été défini avec succès.");
    }
}
