package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "fly",
        description = "Enable fly for a player."
)
@Permission(Permissions.FlyCommand)
public class FlyCommand extends CommandExecutor {
    public FlyCommand() {
        register("flySelf", Collections.emptyList(), "vaultcore");
        register("flyOthers", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())), "vaultcore");
    }

    @SubCommand("flySelf")
    @PlayerOnly
    public void flySelf(CommandSender sender) {
        String string = VaultCore.getInstance().getConfig().getString("string");
        String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

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
        String string = VaultCore.getInstance().getConfig().getString("string");
        String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
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
                    + "enabled" + string + " fly for " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + org.bukkit.ChatColor.RESET)));
            target.setAllowFlight(true);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Your fly has been "
                    + variable1 + "enabled" + string + " by " + variable1 + (sender instanceof Player ? VaultCoreAPI.getName((Player) sender) : ChatColor.BLUE + "" +
                    ChatColor.BOLD + "CONSOLE" + org.bukkit.ChatColor.RESET)));
        }
    }
}