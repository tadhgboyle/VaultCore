package net.vaultmc.vaultcore.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class MsgCommand implements CommandExecutor {

	private static HashMap<UUID, UUID> replies = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("msg")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(Utilities.consoleError());
				return true;
			}
			Player player = (Player) sender;
			if (!player.hasPermission(Permissions.MsgCommand)) {
				player.sendMessage(Utilities.noPermission());
				return true;
			} else if (args.length <= 1) {
				player.sendMessage(Utilities.usageMessage(cmd.getName(), "<player> <message>"));
				return true;
			} else if (args.length >= 2) {
				Player target = Bukkit.getServer().getPlayer(args[0]);

				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}
				if (target == player) {
					player.sendMessage(ChatColor.RED + "You can't message yourself!");
					return true;
				}
				if (VaultCore.getInstance().getPlayerData()
						.getBoolean("players." + target.getUniqueId() + ".settings.msg") == false) {
					player.sendMessage(ChatColor.RED + "That player has disabled Messages!");
					return true;
				} else {

					String message = "";
					for (int i = 1; i != args.length; i++)
						message += args[i] + " ";

					String meTo = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
							+ target.getName() + ChatColor.YELLOW + ":");
					String toMe = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
							+ target.getName() + ChatColor.YELLOW + ":");

					player.sendMessage(meTo + " " + ChatColor.DARK_GREEN + message);
					target.sendMessage(toMe + " " + ChatColor.DARK_GREEN + message);

					replies.put(target.getUniqueId(), player.getUniqueId());

					return true;
				}
			}
		}

		if (commandLabel.equalsIgnoreCase("r")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(Utilities.consoleError());
				return true;
			}

			Player player = (Player) sender;

			if (!player.hasPermission(Permissions.MsgCommand)) {
				player.sendMessage(Utilities.noPermission());
				return true;
			}
			if (args.length < 1) {
				player.sendMessage(Utilities.usageMessage(commandLabel, "<message>"));
				return true;
			}

			Player target = Bukkit.getPlayer(replies.get(player.getUniqueId()));

			if (target == null) {
				player.sendMessage(ChatColor.RED + "That player is now offline!");
				return true;
			}
			if (VaultCore.getInstance().getPlayerData()
					.getBoolean("players." + target.getUniqueId() + ".settings.msg") == false) {
				player.sendMessage(ChatColor.RED + "That player has disabled Messages!");
				return true;
			}
			if (replies.containsKey(player.getUniqueId())) {
				String message = "";
				for (String s : args) {
					message = message + s + " ";
				}
				String meTo = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
						+ target.getName() + ChatColor.YELLOW + ":");
				String toMe = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
						+ target.getName() + ChatColor.YELLOW + ":");
				player.sendMessage(meTo + " " + ChatColor.DARK_GREEN + message);
				target.sendMessage(toMe + " " + ChatColor.DARK_GREEN + message);
				replies.put(target.getUniqueId(), player.getUniqueId());
			} else {
				player.sendMessage(ChatColor.RED + "You do not have anyone to reply to!");
				return true;
			}
		}
		return true;
	}
}