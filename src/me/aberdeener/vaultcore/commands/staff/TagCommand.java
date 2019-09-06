package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class TagCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		String string = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("string"));
		String variable1 = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("variable-1"));

		// base command
		if (commandLabel.equalsIgnoreCase("tag")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			Player player = (Player) sender;

			if (!sender.hasPermission("vc.tag")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			else {

				if (args.length < 2) {
					player.sendMessage(
							ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tag <player|[delete]> <message>");
					return true;
				}
				
				if (args[0] == "delete") {
					// delete from list
				}

				else {
					// add to list
				}
			}
		}

		return true;
	}
}