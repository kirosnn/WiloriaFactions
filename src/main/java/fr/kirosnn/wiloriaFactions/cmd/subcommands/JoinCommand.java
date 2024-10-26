package fr.kirosnn.wiloriaFactions.cmd.subcommands;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.data.Faction;
import fr.kirosnn.wiloriaFactions.data.Factions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JoinCommand {
    private final WiloriaFactions plugin;
    private final Factions factions;

    private static final String USAGE_MESSAGE = ChatColor.RED + "Usage: /f join <nomdelafaction>";
    private static final String PLAYER_ONLY_MESSAGE = ChatColor.RED + "Seuls les joueurs peuvent utiliser cette commande.";
    private static final String NO_INVITATION_MESSAGE = ChatColor.RED + "Vous n'avez pas été invité à rejoindre cette faction.";
    private static final String FACTION_NOT_FOUND_MESSAGE = ChatColor.RED + "La faction %s n'existe pas.";
    private static final String JOIN_SUCCESS_MESSAGE = ChatColor.GREEN + "Vous avez rejoint la faction %s !";
    private static final String MEMBER_JOINED_MESSAGE = ChatColor.YELLOW + "%s a rejoint votre faction !";

    public JoinCommand(WiloriaFactions plugin) {
        this.plugin = plugin;
        this.factions = plugin.getFactions();
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PLAYER_ONLY_MESSAGE);
            return;
        }

        if (args.length < 2) {
            player.sendMessage(USAGE_MESSAGE);
            return;
        }

        String factionName = args[1];
        Faction faction = factions.getFactionByName(factionName);
        if (faction == null) {
            player.sendMessage(String.format(FACTION_NOT_FOUND_MESSAGE, factionName));
            return;
        }

        if (!factions.isPlayerInvited(factionName, player.getUniqueId())) {
            player.sendMessage(NO_INVITATION_MESSAGE);
            return;
        }

        factions.addPlayerToFaction(player.getUniqueId(), faction);
        factions.removeInvitation(factionName, player.getUniqueId());
        player.sendMessage(String.format(JOIN_SUCCESS_MESSAGE, faction.getName()));

        broadcastToFactionMembers(faction, String.format(MEMBER_JOINED_MESSAGE, player.getName()));
    }

    private void broadcastToFactionMembers(Faction faction, String message) {
        for (UUID memberId : faction.getMembers()) {
            Player factionMember = plugin.getServer().getPlayer(memberId);
            if (factionMember != null && factionMember.isOnline()) {
                factionMember.sendMessage("§7§l(§e§lFaction§7§l) " + message);
            }
        }
    }
}
