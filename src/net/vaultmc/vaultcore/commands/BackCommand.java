package net.vaultmc.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.listeners.PlayerTPListener;

public class BackCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("back")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!player.hasPermission(Permissions.BackCommand)) {

				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			} else if (PlayerTPListener.teleports.containsKey(player.getUniqueId())) {

				String string = ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("string"));
				Location before = PlayerTPListener.teleports.get(player.getUniqueId());
				player.teleport(before);
				player.sendMessage(string + "You have been teleported to your previous location...");
				PlayerTPListener.teleports.remove(player.getUniqueId());
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You have nowhere to teleport to!");
				return true;
			}
		}
		return true;
	}
}
