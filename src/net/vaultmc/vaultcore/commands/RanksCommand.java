package net.vaultmc.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;

public class RanksCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("ranks")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utilities.consoleError());
				return true;
			}
			
			Player player = (Player) sender;
			
			if (!player.hasPermission(Permissions.RanksCommand)) {
				player.sendMessage(Utilities.noPermission());
				return true;
			} 
			else {
				player.sendMessage(ChatColor.DARK_GREEN + "--== [Player Ranks] ==--");
				player.sendMessage(ChatColor.DARK_GRAY + "Default");
				player.sendMessage(ChatColor.GRAY + "Member");
				player.sendMessage(ChatColor.WHITE + "Patreon");
				player.sendMessage(ChatColor.AQUA + "Trusted");
				player.sendMessage("");
				player.sendMessage(ChatColor.DARK_GREEN + "--== [Staff Ranks] ==--");
				player.sendMessage(ChatColor.DARK_AQUA + "Moderator");
				player.sendMessage(ChatColor.BLUE + "Administrator");
				return true;
			}
		}
		return true;
	}
}