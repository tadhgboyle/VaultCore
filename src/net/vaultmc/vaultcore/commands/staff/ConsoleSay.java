package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class ConsoleSay implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("say") || (commandLabel.equalsIgnoreCase("chat"))) {

			if (!(sender instanceof Player)) {

				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/say <message>");
				} else {
					String message = "";
					for (String s : args) {
						message = message + s + " ";
					}
					String csay = String.format(ChatColor.BLUE + "" + ChatColor.BOLD + "" + "CONSOLE"
							+ ChatColor.DARK_GRAY + " → " + ChatColor.WHITE + "%s", message);

					for (Player player : Bukkit.getOnlinePlayers()) {
						player.sendMessage(csay);
					}
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
		}
		return true;
	}
}