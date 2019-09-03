package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;
import net.md_5.bungee.api.ChatColor;

public class GrantCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		// base command
		if (commandLabel.equalsIgnoreCase("grant")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			
			// permission check
			if (!sender.hasPermission("vc.grant")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			// if they do not enter a username/argument
			if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/grant <player>");
				return true;
			}

			else if (args.length == 1) {

				Player target = Bukkit.getServer().getPlayer(args[0]);

				// if entered player is offline, say so
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}

				// if they enter themselves
				if (target == sender) {
					sender.sendMessage(ChatColor.RED + "You can't give yourself a rank!");
					return true;
				}

				if (sender.hasPermission("vc.grant.admin")) {

					// else, open the inventory with target as paramater
					Player player = (Player) sender;
					player.openInventory(GrantCommandInv.getGrantInventoryAdmin(target));
				}

				else if (sender.hasPermission("vc.grant.mod")) {

					// else, open the inventory with target as paramater
					Player player = (Player) sender;
					player.openInventory(GrantCommandInv.getGrantInventoryMod(target));
				}
			}

			// if they enter no argument, or more than one...
			else {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/grant <player>");

			}
		}
		return true;
	}
}