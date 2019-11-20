package net.vaultmc.vaultcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;

public class ECCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("enderchest")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(Utilities.consoleError());
				return true;
			}
			Player player = (Player) sender;
			if (!player.hasPermission(Permissions.ECCommand)) {
				player.sendMessage(Utilities.noPermission());
				return true;
			} else {
				if (args.length == 0) {
					player.openInventory(player.getEnderChest());
					return true;
				}

				if (!player.hasPermission(Permissions.ECCommandOther)) {
					player.sendMessage(Utilities.managePlayerError(cmd.getName()));
					return true;
				}

				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}

				player.openInventory(target.getEnderChest());
				return true;
			}
		}
		return true;
	}
}