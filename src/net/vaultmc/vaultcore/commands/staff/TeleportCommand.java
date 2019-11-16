package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class TeleportCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("teleport") || commandLabel.equalsIgnoreCase("tp")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(VaultCore.getInstance().getConfig().getString("console-error"));
				return true;
			}

			Player player = (Player) sender;

			if (!player.hasPermission("vc.teleport")) {
				player.sendMessage(VaultCore.getInstance().getConfig().getString("no-permission"));
				return true;
			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);

				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}

				if (target == player) {
					player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
					return true;
				}

				Location targetLoc = target.getLocation();
				player.teleport(targetLoc);
				player.sendMessage(string + "Teleported to " + variable1 + target.getName() + string + ".");
				return true;
			}

			if (args.length == 2) {
				if (!player.hasPermission("vc.teleport.other")) {
					player.sendMessage(
							ChatColor.DARK_RED + "Uh oh! You don't have permission to teleport that player!");
					return true;
				}

				Player target1 = Bukkit.getServer().getPlayer(args[0]);
				Player target2 = Bukkit.getServer().getPlayer(args[1]);

				if (target1 == player || target2 == player) {
					player.sendMessage(ChatColor.RED + "This is confusing...");
					return true;
				}

				if (target1 == null) {
					player.sendMessage(string + "The player " + variable1 + args[0] + string + " is offline.");
					return true;
				}

				if (target2 == null) {
					player.sendMessage(string + "The player " + variable1 + args[1] + string + " is offline.");
					return true;
				}

				Location targetLoc = target2.getLocation();
				target1.teleport(targetLoc);

				player.sendMessage(string + "Teleported " + variable1 + target1.getName() + string + " to " + variable1
						+ target2.getName() + string + ".");
				target1.sendMessage(variable1 + player.getName() + string + " has teleported you to " + variable1
						+ target2.getName() + string + ".");
				target2.sendMessage(variable1 + player.getName() + string + " has teleported " + variable1
						+ target1.getName() + string + " to you.");
				return true;
			}

			else {
				player.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tp <player> [player]");
				return true;
			}

		}

		if (commandLabel.equalsIgnoreCase("tphere") || commandLabel.equalsIgnoreCase("tph")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			Player player = (Player) sender;

			if (!player.hasPermission("vc.teleport.here")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (args.length == 1) {
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}
				if (target == player) {
					player.sendMessage(ChatColor.RED + "You can't teleport yourself to yourself!");
					return true;
				}
				Location playerLoc = ((Entity) player).getLocation();
				target.teleport(playerLoc);
				player.sendMessage(string + "Teleported " + variable1 + target.getName() + string + " to you.");
				return true;
			} else {
				player.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tp <player> [player]");
				return true;
			}
		}
		return true;
	}
}
