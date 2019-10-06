package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;
import net.md_5.bungee.api.ChatColor;

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
			if (args.length == 1) {

				Player target = Bukkit.getServer().getPlayer(args[0]);

				if (target == null) {
					sender.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}
				if (target == sender) {
					sender.sendMessage(ChatColor.RED + "You can't give yourself a rank!");
					return true;
				}
				if (sender.hasPermission("vc.grant.admin")) {
					Player player = (Player) sender;
					player.openInventory(GrantCommandInv.getGrantInventoryAdmin(target));
				}
				else if (sender.hasPermission("vc.grant.mod")) {
					Player player = (Player) sender;
					player.openInventory(GrantCommandInv.getGrantInventoryMod(target));
				}
			}
		}
		return true;
	}
}