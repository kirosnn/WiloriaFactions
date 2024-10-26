/*
FactionsCommand.java permet de gérer le /f
 */

package fr.kirosnn.wiloriaFactions.cmd;

import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.cmd.subcommands.*;
import fr.kirosnn.wiloriaFactions.cdf.Items;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsCommand implements CommandExecutor {
    private final CreateFactionCommand createFactionCommand;
    private final DescriptionCommand descriptionCommand;
    private final CoreCommand coreCommand;
    private final HelpCommand helpCommand;
    private final ClaimCommand claimCommand;
    private final UnclaimCommand unclaimCommand;
    private final UnclaimAllCommand unclaimAllCommand;
    private final MapCommand mapCommand;
    private final InviteCommand inviteCommand;
    private final JoinCommand joinCommand;
    private final PromoteCommand promoteCommand;
    private  final DemoteCommand demoteCommand;

    public FactionsCommand(WiloriaFactions plugin) {
        this.createFactionCommand = new CreateFactionCommand(plugin.getFactions());
        this.descriptionCommand = new DescriptionCommand(plugin);
        this.coreCommand = new CoreCommand(plugin, new Items());
        this.helpCommand = new HelpCommand();
        this.claimCommand = new ClaimCommand(plugin);
        this.unclaimCommand = new UnclaimCommand(plugin);
        this.unclaimAllCommand = new UnclaimAllCommand(plugin);
        this.mapCommand = new MapCommand(plugin.getFactions());
        this.joinCommand = new JoinCommand(plugin);
        this.inviteCommand = new InviteCommand(plugin);
        this.promoteCommand = new PromoteCommand();
        this.demoteCommand = new DemoteCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande ne peut être utilisée que par un joueur !");
            return true;
        }

        if (args.length == 0) {
            helpCommand.execute(player, new String[]{"help", "1"});
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                createFactionCommand.execute(player, args);
                break;
            case "desc":
                descriptionCommand.execute(player, args);
                break;
            case "core":
                coreCommand.execute(player);
                break;
            case "help":
                helpCommand.execute(player, args);
                break;
            case "claim":
                claimCommand.execute(player);
                break;
            case "unclaim":
                unclaimCommand.execute(player);
                break;
            case "unclaimall":
                unclaimAllCommand.execute(player);
                break;
            case "map":
                mapCommand.execute(player);
                break;
            case "invite":
                inviteCommand.execute(player, args);
                break;
            case "join":
                joinCommand.execute(player, args);
                break;
            case "promote":
                promoteCommand.execute(player, args);
                break;
            case "demote":
                demoteCommand.execute(player, args);
                break;
            default:
                player.sendMessage("§cCommande inconnue. Tapez /f pour voir les commandes disponibles.");
                helpCommand.execute(player, new String[]{"help", "1"});
                break;
        }
        return true;
    }
}
