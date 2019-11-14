package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class GamemodeCommand implements CommandExecutor {

	String string = (ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string")));
	String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("gamemode")) {

			if (!sender.hasPermission("vc.gamemode")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(
						ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/gamemode <mode> [player]");
				return true;
			}
			else if (args[0].equalsIgnoreCase("creative")) {

				if (!sender.hasPermission("vc.gamemode.creative")) {
					sender.sendMessage(ChatColor.DARK_RED + "Uh oh! You don't have permission for creative mode!");
					return true;
				}
				if (args.length == 1) {
					((HumanEntity) sender).setGameMode(GameMode.CREATIVE);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "Your gamemode has been set to " + variable1 + "creative"));
					return true;
				}
				else if (args.length == 2) {
					if (!sender.hasPermission("vc.gamemode.other")) {
						sender.sendMessage(
								ChatColor.DARK_RED + "Uh oh! You don't have permission to set their gamemode!");
						return true;
					}
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					else {
						target.setGameMode(GameMode.CREATIVE);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + target.getName()
								+ string + "'s gamemode has been set to " + variable1 + "creative" + string + "."));
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + sender.getName()
								+ string + " has set your gamemode to " + variable1 + "creative" + string + "."));
						return true;
					}
				}
			}
			else if (args[0].equalsIgnoreCase("survival")) {

				if (!sender.hasPermission("vc.gamemode.survival")) {
					sender.sendMessage(ChatColor.DARK_RED + "Uh oh! You don't have permission for survial mode!");
					return true;
				}

				if (args.length == 1) {
					((HumanEntity) sender).setGameMode(GameMode.SURVIVAL);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "Your gamemode has been set to " + variable1 + "survival" + string + "."));
					return true;
				}

				else if (args.length == 2) {
					if (!sender.hasPermission("vc.gamemode.other")) {
						sender.sendMessage(
								ChatColor.DARK_RED + "Uh oh! You don't have permission to set their gamemode!");
						return true;
					}
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					else {
						target.setGameMode(GameMode.SURVIVAL);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + target.getName()
								+ string + "'s gamemode has been set to " + variable1 + "survival" + string + "."));
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + sender.getName()
								+ string + " has set your gamemode to " + variable1 + "survival" + string + "."));
						return true;
					}
				}
			}
			else if (args[0].equalsIgnoreCase("spectator")) {

				if (!sender.hasPermission("vc.gamemode.spectator")) {
					sender.sendMessage(ChatColor.DARK_RED + "Uh oh! You don't have permission for spectator mode!");
					return true;
				}
				if (args.length == 1) {
					((HumanEntity) sender).setGameMode(GameMode.SPECTATOR);
					sender.sendMessage(ChatColor.YELLOW + "Your gamemode has been set to " + ChatColor.GOLD
							+ "spectator" + string + ".");
					return true;
				}
				else if (args.length == 2) {
					if (!sender.hasPermission("vc.gamemode.other")) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "Uh oh! You don't have permission to set their gamemode!" + string + ".");
						return true;
					}
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					else {
						target.setGameMode(GameMode.SPECTATOR);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + target.getName()
								+ string + "'s gamemode has been set to " + variable1 + "spectator" + string + "."));
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + sender.getName()
								+ string + " has set your gamemode to " + variable1 + "spectator" + string + "."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(
						ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/gamemode <mode> [player]");
				return true;
			}
		}
		if (command.getName().equalsIgnoreCase("gmc")) {

			if (!sender.hasPermission("vc.gamemode.creative")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			else {
				((HumanEntity) sender).setGameMode(GameMode.CREATIVE);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "Your gamemode has been set to " + variable1 + "creative" + string + "."));
				return true;
			}
		}
		if (command.getName().equalsIgnoreCase("gms")) {

			if (!sender.hasPermission("vc.gamemode.survival")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			else {
				((HumanEntity) sender).setGameMode(GameMode.SURVIVAL);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "Your gamemode has been set to " + variable1 + "survival" + string + "."));
				return true;
			}

		}
		if (command.getName().equalsIgnoreCase("gmsp")) {

			if (!sender.hasPermission("vc.gamemode.spectator")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			else {
				((HumanEntity) sender).setGameMode(GameMode.SPECTATOR);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "Your gamemode has been set to " + variable1 + "spectator" + string + "."));
				return true;
			}
		}
		return true;
	}
}