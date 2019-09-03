package me.aberdeener.vaultcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import me.aberdeener.vaultcore.VaultCore;

public class WorkbenchCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		// base command
		if (commandLabel.equalsIgnoreCase("workbench")) {

			Player player = (Player) sender;

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;

			}

			if (!sender.hasPermission("vc.workbench")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			else {

				Inventory craftingTable = Bukkit.getServer().createInventory(null, InventoryType.WORKBENCH);
				player.openInventory(craftingTable);

			}
		}
		return true;
	}
}