package me.aberdeener.vaultcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class ECCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("enderchest")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.enderchest")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			else {
				if (args.length == 0) {
					player.openInventory(player.getEnderChest());
					return true;
				}
				
				if (!player.hasPermission("vc.enderchest.other")) {
					player.sendMessage(ChatColor.DARK_RED + "Uh oh! You don't have permission to look at their enderchest!");
					return true;
				}

				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}

				player.openInventory(target.getEnderChest());
				return true;
			}
		}
		return true;
	}
}