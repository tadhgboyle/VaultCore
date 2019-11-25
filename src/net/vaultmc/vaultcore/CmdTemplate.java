package net.vaultmc.vaultcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTemplate implements CommandExecutor {

    String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));
    String variable2 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-2"));

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Utilities.consoleError());
            return true;
        }

        Player player = (Player) sender;

        if (commandLabel.equalsIgnoreCase("XXXXX")) {

            if (!player.hasPermission(Permissions.CmdTemplate)) {
                player.sendMessage(Utilities.noPermission());
                return true;
            }

            if (args.length != 0) {
                player.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED
                        + "/command <required arg> [optional arg]");
                return true;
            }

            // do stuff

        }
        return true;
    }
}