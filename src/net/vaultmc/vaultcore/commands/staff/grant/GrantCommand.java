package net.vaultmc.vaultcore.commands.staff.grant;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;

public class GrantCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("grant")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(Utilities.consoleError());
				return true;
			}

			Player player = (Player) sender;

			if (!player.hasPermission(Permissions.GrantCommandAdmin)
					|| !player.hasPermission(Permissions.GrantCommandMod)) {
				player.sendMessage(Utilities.noPermission());
				return true;
			}
			if (args.length != 1) {
				player.sendMessage(Utilities.usageMessage(cmd.getName(), "<player>"));
				return true;
			}

			Player target = Bukkit.getServer().getPlayer(args[0]);

			if (target == null) {
				player.sendMessage(ChatColor.RED + "That player is offline!");
				return true;
			}
			if (target == player) {
				player.sendMessage(ChatColor.RED + "You can't give yourself a rank!");
				return true;
			}
			if (player.hasPermission(Permissions.GrantCommandAdmin)) {
				player.openInventory(GrantCommandInv.getGrantInventoryAdmin(target));
				return true;
			} else if (player.hasPermission(Permissions.GrantCommandMod)) {
				player.openInventory(GrantCommandInv.getGrantInventoryMod(target));
				return true;
			}

		}
		return true;
	}
}