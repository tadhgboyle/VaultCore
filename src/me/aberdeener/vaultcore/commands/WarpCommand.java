package me.aberdeener.vaultcore.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class WarpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		// warp command
		if (commandLabel.equalsIgnoreCase("warp")) {

			// console sender check
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

				if ((VaultCore.getInstance().getConfig().getConfigurationSection("warps." + args[0])) == null) {
					player.sendMessage(ChatColor.YELLOW + "The warp " + ChatColor.GOLD + args[0] + ChatColor.YELLOW
							+ " does not exist!");
					return true;
				}

				else {

					Location warp = new Location(
							Bukkit.getWorld((UUID) VaultCore.getInstance().getConfig().get("warps." + args[0] + ".world")),
							VaultCore.getInstance().getConfig().getDouble("warps." + args[0] + ".x"),
							VaultCore.getInstance().getConfig().getDouble("warps." + args[0] + ".y"),
							VaultCore.getInstance().getConfig().getDouble("warps." + args[0] + ".z"));

					player.teleport(warp);
					sender.sendMessage(ChatColor.YELLOW + "You have been teleported to " + ChatColor.GOLD + args[0]
							+ ChatColor.YELLOW + "!");
					return true;
				}

			}

			else {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/warp <warp>");
				return true;
			}

		}

		// setwarp command
		if (commandLabel.equalsIgnoreCase("setwarp")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "[vaultsuite] Please execute from in-game!");
				return true;
			}

			Player player = (Player) sender;

			if (!sender.hasPermission("vc.warp.set")) {
				sender.sendMessage(ChatColor.DARK_RED + "Uh oh! You don't have permission!");
				return true;
			}

			else if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/setwarp <name>");
				return true;
			}

			else {

				VaultCore.getInstance().getConfig().set("warps." + args[0] + ".world",
						player.getLocation().getWorld().getName());
				VaultCore.getInstance().getConfig().set("warps." + args[0] + ".x", player.getLocation().getX());
				VaultCore.getInstance().getConfig().set("warps." + args[0] + ".y", player.getLocation().getY());
				VaultCore.getInstance().getConfig().set("warps." + args[0] + ".z", player.getLocation().getZ());
				VaultCore.getInstance().saveConfig();

				player.sendMessage(
						ChatColor.YELLOW + "Warp " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " has been set!");
				return true;

			}
		}

		return true;
	}
}