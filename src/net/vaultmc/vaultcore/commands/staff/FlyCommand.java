package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class FlyCommand implements CommandExecutor {

	private boolean active = false;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

		if (!(sender instanceof Player)) {
			sender.sendMessage(Utilities.consoleError());
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission(Permissions.FlyCommand)) {
			player.sendMessage(Utilities.noPermission());
			return true;
		}

		if (args.length == 0) {
			if (!active) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You have " + variable1 + "enabled" + string + " fly."));
				this.active = true;
				player.setAllowFlight(true);
				return true;
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You have " + variable1 + "disabled" + string + " fly."));
				active = false;
				player.setFlying(false);
				player.setAllowFlight(false);
				return true;
			}
		}

		if (args.length == 1) {
			if (!player.hasPermission(Permissions.FlyCommandOther)) {
				player.sendMessage(Utilities.managePlayerError(cmd.getName()));
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if (target != null) {
				if (!active) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1
							+ "enabled" + string + " fly for " + variable1 + target.getName()));
					this.active = true;
					target.setAllowFlight(true);
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Your fly has been "
							+ variable1 + "enabled" + string + " by " + variable1 + player.getName()));
					return true;
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1
							+ "disabled" + string + " fly for " + variable1 + target.getName()));
					active = false;
					target.setFlying(false);
					target.setAllowFlight(false);
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Your fly has been "
							+ variable1 + "disabled" + string + " by " + variable1 + player.getName()));
					return true;
				}
			} else {
				player.sendMessage(ChatColor.RED + "That player is not online!");
				return true;
			}
		}
		player.sendMessage(Utilities.usageMessage(cmd.getName(), "[player]"));
		return true;
	}
}