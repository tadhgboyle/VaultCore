package net.vaultmc.vaultcore.commands.staff.grant;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;

public class GrantCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("grant")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			if (!sender.hasPermission("vc.grant")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/grant <player>");
				return true;
			}

			Player target = Bukkit.getServer().getPlayer(args[0]);

			if (target == null) {
				sender.sendMessage(ChatColor.RED + "That player is offline!");
				return true;
			}
			if (target == sender) {
				sender.sendMessage(ChatColor.RED + "You can't give yourself a rank!");
				return true;
			}
			if (sender.hasPermission(Permissions.GrantCommandAdmin)) {
				Player player = (Player) sender;
				player.openInventory(GrantCommandInv.getGrantInventoryAdmin(target));
				return true;
			} else if (sender.hasPermission(Permissions.GrantCommandMod)) {
				Player player = (Player) sender;
				player.openInventory(GrantCommandInv.getGrantInventoryMod(target));
				return true;
			}

		}
		return true;
	}
}