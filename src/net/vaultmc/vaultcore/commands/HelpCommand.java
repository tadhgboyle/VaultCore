package net.vaultmc.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;

public class HelpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("help")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!player.hasPermission(Permissions.HelpCommand)) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
			} 
			else {
				if (args.length == 0) {
					player.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RED + "/help <option> [page]");
					player.sendMessage(ChatColor.GOLD + "Available Options:");
					player.spigot().sendMessage(
							Utilities.hoverMaker("General", "Helpful commands to be used universally!", "/help general"),
							Utilities.messageMakerString(", "),
							Utilities.hoverMaker("Creative", "Commands to use in the Creative world!", "/help creative"),
							Utilities.messageMakerString(", "),
							Utilities.hoverMaker("Survival", "Commands to use in the Survival world!", "/help survival"),
							Utilities.messageMakerString(", "),
							Utilities.hoverMaker("Clans", "Commands to use in the Clans world!", "/help clans"),
							Utilities.messageMakerString(", "),
							Utilities.hoverMaker("SkyBlock", "Commands to use in the SkyBlock world!", "/help skyblock"));
					return true;
				} 
				else if (args[0].equalsIgnoreCase("general")) {
					player.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.GOLD + "General Commands:");
					player.sendMessage(ChatColor.YELLOW + "/token - Get Your VaultMC Services token.");
					player.sendMessage(ChatColor.YELLOW + "/discord - Join our Discord.");
					player.sendMessage(ChatColor.YELLOW + "/msg <player> - Send a message to a player.");
					player.sendMessage(ChatColor.YELLOW + "/tpa - Send a teleport request.");
					player.sendMessage(ChatColor.YELLOW + "/tpaccept - Accept a teleport request.");
					player.sendMessage(ChatColor.YELLOW + "/spawn - Return to spawn.");
					player.sendMessage(ChatColor.YELLOW + "/playtime [player] - Check your PlayTime.");
					return true;
				} 
				else if (args[0].equalsIgnoreCase("creative")) {
					player.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.GOLD + "Creative Commands:");
					player.sendMessage(ChatColor.YELLOW + "/plot auto - Claim a plot.");
					player.sendMessage(ChatColor.YELLOW + "/plot home [player] - Go to a plot.");
					player.sendMessage(ChatColor.YELLOW + "/plot add <player> - Let a player build on your plot.");
					player.sendMessage(ChatColor.YELLOW + "/plot remove <player> - Do not a player build on your plot.");
					return true;
				} 
				else if (args[0].equalsIgnoreCase("survival")) {
					player.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.GOLD + "Survival Commands:");
					player.sendMessage(ChatColor.YELLOW + "/wild - Teleport randomly in the world.");
					return true;
				}
				else if (args[0].equalsIgnoreCase("clans")) {
					player.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.GOLD + "Clans Commands:");
					player.sendMessage(ChatColor.YELLOW + "/wild - Teleport randomly in the world.");
					player.sendMessage(ChatColor.YELLOW + "/c join <clan> - Join a clan.");
					player.sendMessage(ChatColor.YELLOW + "/c home - Go to your clan home.");
					player.sendMessage(ChatColor.YELLOW + "/c auction - Open the clan auction GUI.");
					return true;
				} 
				else if (args[0].equalsIgnoreCase("skyblock")) {
					player.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					player.sendMessage(" ");
					player.sendMessage(ChatColor.GOLD + "SkyBlock Commands:");
					player.sendMessage(ChatColor.YELLOW + "/island - Get your own island.");
					player.sendMessage(ChatColor.YELLOW + "/is - Go to your island");
					player.sendMessage(ChatColor.YELLOW + "/island top - View the top islands.");
					return true;
				} 
				else {
					player.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/help <option>");
				return true;
				}
			}
		}
		return true;
	}
}