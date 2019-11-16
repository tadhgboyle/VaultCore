package net.vaultmc.vaultcore.commands.settings;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class SettingsCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("settings")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;

			if (!sender.hasPermission("vc.settings")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			} else {
				SettingsInventories.init(player);
				player.openInventory(SettingsInventories.SettingsMain(player));
				return true;
			}
		}
		return true;
	}
}