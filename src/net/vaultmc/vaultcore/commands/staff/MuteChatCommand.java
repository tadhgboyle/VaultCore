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

public class MuteChatCommand implements CommandExecutor {

    public static boolean mutechat = false;

    String string = VaultCore.getInstance().getConfig().getString("string");
    String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("mutechat")) {

            if (!(sender instanceof Player)) {
                if (mutechat) {
                    mutechat = false;
                    Bukkit.broadcastMessage(
                            ChatColor.translateAlternateColorCodes('&', string + "The chat has been unmuted!"));
                    return true;
                } else {
                    mutechat = true;
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            variable1 + "CONSOLE " + string + "has muted the chat!"));
                    return true;
                }
            }

            Player player = (Player) sender;

            if (!player.hasPermission(Permissions.MuteChatCommand)) {
                sender.sendMessage(Utilities.noPermission());
                return true;
            }
            if (mutechat) {
                mutechat = false;
                Bukkit.broadcastMessage(
                        ChatColor.translateAlternateColorCodes('&', string + "The chat has been unmuted!"));
                return true;

            } else {
                mutechat = true;
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        string + "The chat has been muted by " + variable1 + sender.getName()));
                return true;
            }
        }
        return true;
    }
}
