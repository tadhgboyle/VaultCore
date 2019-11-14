package net.vaultmc.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;

public class HelpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("help")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!player.hasPermission("vc.help")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
			} 
			else {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RED + "/help <option> [page]");
					sender.sendMessage(ChatColor.GOLD + "Available Options:");
					player.spigot().sendMessage(
							VaultCoreAPI.hoverMaker("General", "Helpful commands to be used universally!", "/help general"),
							VaultCoreAPI.messageMakerString(", "),
							VaultCoreAPI.hoverMaker("Creative", "Commands to use in the Creative world!", "/help creative"),
							VaultCoreAPI.messageMakerString(", "),
							VaultCoreAPI.hoverMaker("Survival", "Commands to use in the Survival world!", "/help survival"),
							VaultCoreAPI.messageMakerString(", "),
							VaultCoreAPI.hoverMaker("Clans", "Commands to use in the Clans world!", "/help clans"),
							VaultCoreAPI.messageMakerString(", "),
							VaultCoreAPI.hoverMaker("SkyBlock", "Commands to use in the SkyBlock world!", "/help skyblock"));
					return true;
				} 
				else if (args[0].equalsIgnoreCase("general")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "General Commands:");
					sender.sendMessage(ChatColor.YELLOW + "/token - Get Your VaultMC Services token.");
					sender.sendMessage(ChatColor.YELLOW + "/discord - Join our Discord.");
					sender.sendMessage(ChatColor.YELLOW + "/msg <player> - Send a message to a player.");
					sender.sendMessage(ChatColor.YELLOW + "/tpa - Send a teleport request.");
					sender.sendMessage(ChatColor.YELLOW + "/tpaccept - Accept a teleport request.");
					sender.sendMessage(ChatColor.YELLOW + "/spawn - Return to spawn.");
					sender.sendMessage(ChatColor.YELLOW + "/playtime [player] - Check your PlayTime.");
					return true;
				} 
				else if (args[0].equalsIgnoreCase("creative")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "Creative Commands:");
					sender.sendMessage(ChatColor.YELLOW + "/plot auto - Claim a plot.");
					sender.sendMessage(ChatColor.YELLOW + "/plot home [player] - Go to a plot.");
					sender.sendMessage(ChatColor.YELLOW + "/plot add <player> - Let a player build on your plot.");
					sender.sendMessage(ChatColor.YELLOW + "/plot remove <player> - Do not a player build on your plot.");
					return true;
				} 
				else if (args[0].equalsIgnoreCase("survival")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "Survival Commands:");
					sender.sendMessage(ChatColor.YELLOW + "/wild - Teleport randomly in the world.");
					return true;
				}
				else if (args[0].equalsIgnoreCase("clans")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "Clans Commands:");
					sender.sendMessage(ChatColor.YELLOW + "/wild - Teleport randomly in the world.");
					sender.sendMessage(ChatColor.YELLOW + "/c join <clan> - Join a clan.");
					sender.sendMessage(ChatColor.YELLOW + "/c home - Go to your clan home.");
					sender.sendMessage(ChatColor.YELLOW + "/c auction - Open the clan auction GUI.");
					return true;
				} 
				else if (args[0].equalsIgnoreCase("skyblock")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "SkyBlock Commands:");
					sender.sendMessage(ChatColor.YELLOW + "/island - Get your own island.");
					sender.sendMessage(ChatColor.YELLOW + "/is - Go to your island");
					sender.sendMessage(ChatColor.YELLOW + "/island top - View the top islands.");
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