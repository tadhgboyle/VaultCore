package me.aberdeener.vaultcore.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class MsgCommand implements CommandExecutor {

	private static HashMap<UUID, UUID> replies = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("msg")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.msg")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			} else if (args.length <= 1) {
				sender.sendMessage(
						ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/msg <player> <message>");
				return true;
			} else if (args.length >= 2) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}
				if (target == sender) {
					sender.sendMessage(ChatColor.RED + "You can't message yourself!");
					return true;
				} else {

					String message = "";
					for (int i = 1; i != args.length; i++)
						message += args[i] + " ";

					String meTo = (ChatColor.GOLD + sender.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
							+ target.getName() + ChatColor.YELLOW + ":");
					String toMe = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
							+ target.getName() + ChatColor.YELLOW + ":");

					sender.sendMessage(meTo + " " + ChatColor.DARK_GREEN + message);
					target.sendMessage(toMe + " " + ChatColor.DARK_GREEN + message);

					replies.put(target.getUniqueId(), player.getUniqueId());

					return true;
				}
			}
		}

		if (commandLabel.equalsIgnoreCase("r")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.msg")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (args.length < 1) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/r <message>");
				return true;
			}
			Player target = Bukkit.getPlayer(replies.get(player.getUniqueId()));

			if (target == null) {
				sender.sendMessage(ChatColor.RED + "That player is now offline!");
				return true;
			}
			if (replies.containsKey(player.getUniqueId())) {
				String message = "";
				for (String s : args) {
					message = message + s + " ";
				}
				String meTo = (ChatColor.GOLD + sender.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
						+ target.getName() + ChatColor.YELLOW + ":");
				String toMe = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
						+ target.getName() + ChatColor.YELLOW + ":");
				sender.sendMessage(meTo + " " + ChatColor.DARK_GREEN + message);
				target.sendMessage(toMe + " " + ChatColor.DARK_GREEN + message);
				replies.put(target.getUniqueId(), player.getUniqueId());
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have anyone to reply to!");
				return true;
			}
		}
		return true;
	}
}