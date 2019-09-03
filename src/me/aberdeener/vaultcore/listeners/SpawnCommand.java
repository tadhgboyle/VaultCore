package me.aberdeener.vaultcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.aberdeener.vaultcore.VaultCore;

public class SpawnCommand implements CommandExecutor, Listener {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");

		// base command
		if (commandLabel.equalsIgnoreCase("setspawn")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			else {
				Player player = (Player) sender;
				if (commandLabel.equalsIgnoreCase("setspawn")) {
					if (!sender.hasPermission("vc.setspawn")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								VaultCore.getInstance().getConfig().getString("no-permission")));
						return true;
					}

					World world = player.getWorld();
					Location loc = player.getLocation();
					player.sendMessage(
							ChatColor.translateAlternateColorCodes('&', string + "Set Spawn to your location!"));
					world.setSpawnLocation(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
				}

			}
		}

		if (commandLabel.equalsIgnoreCase("spawn")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			else {

				Player player = (Player) sender;
				if (commandLabel.equalsIgnoreCase("spawn")) {
					if (!sender.hasPermission("vc.spawn")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								VaultCore.getInstance().getConfig().getString("no-permission")));
						return true;
					}

					player.sendMessage(
							ChatColor.translateAlternateColorCodes('&', string + "Teleporting you to spawn.."));
					player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
				}

			}

		}

		return true;
	}
}