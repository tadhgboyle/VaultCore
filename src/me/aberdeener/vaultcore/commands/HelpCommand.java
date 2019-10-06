package me.aberdeener.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HelpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("help")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player p = (Player) sender;
			if (!p.hasPermission("vc.help")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
			} else {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RED + "/help <option> [page]");
					sender.sendMessage(ChatColor.GOLD + "Available Options:");
					TextComponent player = new TextComponent(ChatColor.YELLOW + "Player");
					player.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder(ChatColor.YELLOW + "Useful commands for everyday players.").create()));
					player.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help player"));
					TextComponent staff = new TextComponent(ChatColor.YELLOW + "Staff");
					staff.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder(ChatColor.DARK_RED + "TOP SECRET STAFF ONLY COMMANDS!").create()));
					staff.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help staff"));
					TextComponent spacer = new TextComponent(ChatColor.YELLOW + ", ");
					p.spigot().sendMessage(player, spacer, staff);
					return true;
				} else if (args[0].equalsIgnoreCase("player")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.GOLD + "Player Commands:");
					sender.sendMessage(ChatColor.YELLOW + "/afk - Go AFK.");
					sender.sendMessage(ChatColor.YELLOW + "/mail - Check your mail.");
					sender.sendMessage(ChatColor.YELLOW + "/msg <player> - Send a message to a player.");
					sender.sendMessage(
							ChatColor.YELLOW + "/seen <player> - See how long a player has been online for.");
					sender.sendMessage(ChatColor.YELLOW + "/tpaccept - Accept a teleport request.");
					sender.sendMessage(ChatColor.YELLOW + "/spawn - Return to spawn.");
					sender.sendMessage(ChatColor.YELLOW + "/playtime - Check your PlayTime.");
					return true;

				} else if (args[0].equalsIgnoreCase("staff")) {
					if (!p.hasPermission("vc.help.staff")) {
						p.sendMessage(ChatColor.DARK_RED + "Hey! You're not staff!");
					} else {
						sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
						sender.sendMessage(" ");
						sender.sendMessage(ChatColor.GOLD + "Staff Commands:");
						sender.sendMessage(ChatColor.YELLOW + "/check <player> - See some information about a player.");
						sender.sendMessage(ChatColor.YELLOW + "/tempban <player> - Tempban a player.");
						sender.sendMessage(ChatColor.YELLOW + "/ban <player> - Ban a player.");
						sender.sendMessage(ChatColor.YELLOW + "/ban-ip <player> - Ban an IP.");
						sender.sendMessage(
								ChatColor.YELLOW + "/p wea - Toggle admin override on WorldEdit in Creative.");
						return true;
					}
				} else
					p.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/help <option> [page]");
				return true;
			}
		}
		return true;
	}
}