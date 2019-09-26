package me.aberdeener.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class RanksCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// base command
		if (commandLabel.equalsIgnoreCase("ranks")) {
			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			if (!sender.hasPermission("vc.ranks")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			else {
				sender.sendMessage(ChatColor.DARK_GREEN + "--== [Player Ranks] ==--");
				sender.sendMessage(ChatColor.DARK_GRAY + "Default");
				sender.sendMessage(ChatColor.GRAY + "Member");
				sender.sendMessage(ChatColor.WHITE + "Patreon");
				sender.sendMessage(ChatColor.AQUA + "Trusted");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.DARK_GREEN + "--== [Staff Ranks] ==--");
				sender.sendMessage(ChatColor.DARK_AQUA + "Moderator");
				sender.sendMessage(ChatColor.BLUE + "Administrator");
				return true;
			}
		}
		return true;
	}
}