package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.aberdeener.vaultcore.VaultCore;

public class InvseeCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		// base command
		if (commandLabel.equalsIgnoreCase("invsee")) {

			// console sender check
			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
			} 
			
			else {
				
				Player player = (Player) sender;
				
				if (!sender.hasPermission("vc.invsee")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
					return true;
				} 
				
				else if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/invsee <player>");
					return true;
				}

				else if (args.length == 1) {
					
					Player target = Bukkit.getServer().getPlayer(args[0]);
					
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					
					if (target == sender) {
						sender.sendMessage(ChatColor.RED + "Why do you want to look at your own inventory?");
						return true;
					}
					
					Inventory targetInv = target.getInventory();
					player.openInventory(targetInv);
				}
			}
		}
		return true;
	}
}