package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class TeleportCommand implements CommandExecutor {

	String string = VaultCore.getInstance().getConfig().getString("string");
	String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("teleport") || commandLabel.equalsIgnoreCase("tp")) {

			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
			} 
			else {
				if (!sender.hasPermission("vc.teleport")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
					return true;
				} 
				else if (args.length == 0) {
					sender.sendMessage(
							ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tp <player> [player]");
					return true;
				}
				else if (args.length == 1) {
					Player target = Bukkit.getServer().getPlayer(args[0]);
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					if (target == sender) {
						sender.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
						return true;
					}
					Location targetLoc = target.getLocation();
					((Entity) sender).teleport(targetLoc);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "Teleported to " + variable1 + target.getName() + string + "."));
					return true;
				}
				else if (args.length == 2) {
					if (!sender.hasPermission("vc.teleport.other")) {
						sender.sendMessage(
								ChatColor.DARK_RED + "Uh oh! You don't have permission to teleport that player!");
						return true;
					}
					Player target1 = Bukkit.getServer().getPlayer(args[0]);
					if (target1 == sender) {
						sender.sendMessage(ChatColor.RED + "This is confusing...");
						return true;
					}
					if (target1 == null) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								string + "The player " + variable1 + args[0] + string + " is offline."));
						return true;
					}
					Player target2 = Bukkit.getServer().getPlayer(args[1]);
					if (target2 == sender) {
						sender.sendMessage(ChatColor.RED + "This is confusing...");
						return true;
					}
					if (target2 == null) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
								string + "The player " + variable1 + args[1] + string + " is offline."));
						return true;
					}
					Location targetLoc = target2.getLocation();
					((Entity) target1).teleport(targetLoc);
					
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Teleported " + variable1
							+ target1.getName() + string + " to " + variable1 + target2.getName() + string + "."));
					target1.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + sender.getName() + string + " has teleported you to "
							+ variable1 + target2.getName() + string + "."));
					target2.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + sender.getName() + string + " has teleported "
							+ variable1 + target1.getName() + string + " to you."));
					return true;
				}
				else {
					sender.sendMessage(
							ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tp <player> [player]");
					return true;
				}
			}
		}

		if (commandLabel.equalsIgnoreCase("tphere") || commandLabel.equalsIgnoreCase("tph")) {

			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
			} 
			else {
				if (!sender.hasPermission("vc.teleport.here")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
					return true;
				} else if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tphere <player>");
					return true;
				}

				else if (args.length == 1) {
					Player target = Bukkit.getServer().getPlayer(args[0]);
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					if (target == sender) {
						sender.sendMessage(ChatColor.RED + "You can't teleport yourself to yourself!");
						return true;
					}
					Location senderLoc = ((Entity) sender).getLocation();
					((Entity) target).teleport(senderLoc);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Teleported " + variable1 + target.getName()
							+ string + " to you."));
					return true;
				}
			}
		}
		return true;
	}
}
