package net.vaultmc.vaultcore.commands.staff;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.Arguments;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(
        literal = "fly",
        description = "Enable fly for a player."
)
@Permission(Permissions.FlyCommand)
public class FlyCommand extends CommandExecutor {
	
    String string = Utilities.string;
    String variable1 = Utilities.variable1;
    
    public FlyCommand() {
        register("flySelf", Collections.emptyList());
        register("flyOthers", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("flySelf")
    @PlayerOnly
    public void flySelf(CommandSender sender) {

        Player player = (Player) sender;
        if (player.getAllowFlight()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "You have " + variable1 + "enabled" + string + " fly."));
            player.setAllowFlight(true);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "You have " + variable1 + "disabled" + string + " fly."));
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @SubCommand("flyOthers")
    @Permission(Permissions.FlyCommandOther)
    public void flyOthers(CommandSender sender, Player target) {

        if (target.getAllowFlight()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1
                    + "disabled" + string + " fly for " + variable1 + VaultCoreAPI.getName(target)));
            target.setFlying(false);
            target.setAllowFlight(false);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Your fly has been "
                    + variable1 + "disabled" + string + " by " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + org.bukkit.ChatColor.RESET)));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1
                    + "enabled" + string + " fly for " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) target) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + org.bukkit.ChatColor.RESET)));
            target.setAllowFlight(true);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Your fly has been "
                    + variable1 + "enabled" + string + " by " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + org.bukkit.ChatColor.RESET)));
        }
    }
}