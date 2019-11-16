package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.VaultCore;

public class FlyCommand implements CommandExecutor {

	private boolean active = false;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					VaultCore.getInstance().getConfig().getString("console-error")));
		}

		Player player = (Player) sender;

		if (!player.hasPermission("vc.fly")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					VaultCore.getInstance().getConfig().getString("no-permission")));
		}

		if (args.length == 0) {

			if (!active) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You have " + variable1 + "enabled" + string + " fly."));

				this.active = true;
				player.setAllowFlight(true);
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You have " + variable1 + "disabled" + string + " fly."));
				active = false;
				player.setFlying(false);
				player.setAllowFlight(false);
			}
		}

		if (args.length == 1) {
			if (!player.hasPermission("vc.fly.other")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
			}
			Player target = Bukkit.getPlayer(args[0]);
			if (target != null) {
				if (!active) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1
							+ "enabled" + string + " fly for " + variable1 + target.getName()));
					this.active = true;
					target.setAllowFlight(true);
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Your fly has been "
							+ variable1 + "enabled" + string + " by " + variable1 + sender.getName()));
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1
							+ "disabled" + string + " fly for " + variable1 + target.getName()));
					active = false;
					target.setFlying(false);
					target.setAllowFlight(false);
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "Your fly has been "
							+ variable1 + "disabled" + string + " by " + variable1 + sender.getName()));
				}
			} else {
				player.sendMessage(ChatColor.RED + "That player is not online!");
				return true;
			}
			player.sendMessage(ChatColor.RED + "Correct Usage: " + ChatColor.DARK_GREEN + "/feed [player]");
			return true;
		}
		return true;
	}
}