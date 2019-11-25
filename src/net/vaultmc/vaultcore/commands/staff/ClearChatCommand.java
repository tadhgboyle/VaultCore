package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class ClearChatCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        String string = VaultCore.getInstance().getConfig().getString("string");
        String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

        if (commandLabel.equalsIgnoreCase("clearchat") || commandLabel.equalsIgnoreCase("cc")) {

            if (!(sender instanceof Player)) {

                for (int i = 0; i < 200; i++) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(" ");
                    }
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have cleared chat!"));
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        variable1 + "CONSOLE " + string + "has cleared chat!"));
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission(Permissions.ClearChatCommand)) {
                player.sendMessage(Utilities.noPermission());
                return true;
            }
            if (args.length != 0) {
                player.sendMessage(Utilities.usageMessage(commandLabel, ""));
                return true;
            }
            for (int i = 0; i < 200; i++) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(" ");
                }
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have cleared chat!"));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "The chat has been cleared by " + variable1 + player.getName() + string + "!"));
            return true;

        }
        return true;
    }
}