package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class FeedCommand implements CommandExecutor {

	String string = VaultCore.getInstance().getConfig().getString("string");
	String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("feed")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;

			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.feed")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			} 
			else {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "You have been " + variable1 + "fed."));
					player.setFoodLevel(20);
					player.setSaturation(20);
					return true;
				} 
				else if (args.length == 1) {
					if (player.hasPermission("vc.feed.other")) {
						Player target = Bukkit.getPlayer(args[0]);
						if (target == null) {
							sender.sendMessage(ChatColor.RED + "That player is offline!");
							return true;
						}
						if (target == sender) {
							sender.sendMessage(
									ChatColor.RED + "Feed yourself using: " + ChatColor.DARK_GREEN + "/feed");
							return true;
						}
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',
								string + "You have fed " + variable1 + target.getName()));
						target.setFoodLevel(20);
						target.setSaturation(20);
						target.sendMessage(ChatColor.translateAlternateColorCodes('&',
								string + "You have been fed by " + variable1 + player.getName()));
						return true;

					} 
					else {
						sender.sendMessage(
								ChatColor.DARK_RED + "Uh oh! You don't have permission to feed that player!");
						return true;
					}
				}
				sender.sendMessage(ChatColor.RED + "Correct Usage: " + ChatColor.DARK_GREEN + "/feed [player]");
				return true;
			}
		}
		return true;
	}
}