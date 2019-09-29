package me.aberdeener.vaultcore.commands.staff;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class StaffChat implements CommandExecutor {

	public static HashMap<UUID, UUID> toggled = new HashMap<>();

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		// base command
		if (commandLabel.equalsIgnoreCase("staffchat") || commandLabel.equalsIgnoreCase("sc")) {

			// console sender check
			if (sender instanceof ConsoleCommandSender) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/sc <message>");
					return true;
				} else {

					String cmessage = "";
					for (String s : args) {
						cmessage = cmessage + s + " ";
					}

					String cprefix = (ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("staffchat-prefix")));
					String cstaffchat = String.format("%s" + ChatColor.BLUE + "" + ChatColor.BOLD + "CONSOLE"
							+ ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + "%s", cprefix, cmessage);

					for (Player player : Bukkit.getOnlinePlayers()) {
						if (player.hasPermission("vc.sc")) {
							player.sendMessage(cstaffchat);
						}
					}
				}
			} else if (sender instanceof Player) {

				Player p = (Player) sender;

				// first permission check
				if (!p.hasPermission("vc.sc")) {
					p.sendMessage(ChatColor.DARK_RED + "Hey! You're not staff!");
				} else {

					// display all commands + syntax
					if (args.length == 0) {
						p.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/sc <message>");
					}

					else {

						String message = "";
						for (String s : args) {
							message = message + s + " ";
						}

						String prefix = (ChatColor.translateAlternateColorCodes('&',
								VaultCore.getInstance().getConfig().getString("staffchat-prefix")));
						String staffchat = String.format(
								"%s" + ChatColor.GRAY + "%s" + ChatColor.DARK_GRAY + " » " + ChatColor.AQUA + "%s",
								prefix, p.getDisplayName(), message);

						for (Player player : Bukkit.getOnlinePlayers()) {
							if (player.hasPermission("vc.sc")) {
								player.sendMessage(staffchat);
							}
						}
					}
				}
			}
			return true;
		}

		if (commandLabel.equalsIgnoreCase("sctoggle")) {

			if (sender instanceof Player) {

				// first permission check
				if (!sender.hasPermission("vc.sc")) {
					sender.sendMessage(ChatColor.DARK_RED + "Hey! You're not staff!");
				} else {
					Player player = (Player) sender;

					if (toggled.containsKey(player.getUniqueId())) {
						toggled.remove(player.getUniqueId());
						player.sendMessage(string + "You have toggled staffchat " + variable1 + "off" + string + ".");
						return true;
					} else {
						toggled.put(player.getUniqueId(), player.getUniqueId());
						player.sendMessage(string + "You have toggled staffchat " + variable1 + "on" + string + ".");
						return true;
					}
				}
			}
		}

		return false;
	}
}