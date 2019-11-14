package net.vaultmc.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class WarpCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("warp")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.warp")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			else if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/warp <warp>");
				return true;
			}
			else if (args.length == 1) {

				if ((VaultCore.getInstance().getConfig().get("warps." + args[0])) == null) {
					player.sendMessage(string + "The warp " + variable1 + args[0] + string + " does not exist!");
					return true;
				}

				else {

					Location warp = (Location) VaultCore.getInstance().getConfig().get("warps." + args[0]);
					player.teleport(warp);
					sender.sendMessage(string + "You have been teleported to " + variable1 + args[0] + string + "!");
					return true;
				}

			}
			else {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/warp <warp>");
				return true;
			}

		}

		if (commandLabel.equalsIgnoreCase("setwarp")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.warp.set")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/setwarp <name>");
				return true;
			}
			else {

				VaultCore.getInstance().getConfig().set("warps." + args[0], player.getLocation());
				VaultCore.getInstance().saveConfig();

				player.sendMessage(string + "Warp " + variable1 + args[0] + string + " has been set!");
				return true;

			}
		}
		
		if (commandLabel.equalsIgnoreCase("delwarp")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.warp.set")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/delwarp <name>");
				return true;
			}
			else {

				VaultCore.getInstance().getConfig().set("warps." + args[0], null);
				VaultCore.getInstance().saveConfig();

				player.sendMessage(string + "Warp " + variable1 + args[0] + string + " has been deleted!");
				return true;
			}
		}
		return true;
	}
}