package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class HealCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

		// base command
		if (commandLabel.equalsIgnoreCase("heal")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
			}

			Player player = (Player) sender;

			// Permission check
			if (!sender.hasPermission("vc.heal")) {
				// Permission message
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			else {

				if (args.length == 0) {
					// Heals the player
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "You have been " + variable1 + "healed"));
					player.setHealth(20.0D);
					player.setFoodLevel(20);
					player.setSaturation(20);
					return true;
				}

				else {

					// Permission check
					if (player.hasPermission("vc.heal.other")) {
						// If player types a username
						if (args.length == 1) {
							Player target = Bukkit.getPlayer(args[0]);

							if (target == null) {
								sender.sendMessage(ChatColor.RED + "That player is offline!");
								return true;
							}

							if (target == sender) {
								sender.sendMessage(
										ChatColor.RED + "Heal yourself using: " + ChatColor.DARK_GREEN + "/heal");
								return true;
							}

							// Heals the target
							player.sendMessage(ChatColor.translateAlternateColorCodes('&',
									string + "You have healed" + variable1 + target.getName()));
							target.setFoodLevel(20);
							target.setHealth(20.0D);
							target.setSaturation(20);
							target.sendMessage(ChatColor.translateAlternateColorCodes('&',
									string + "You have been healed by" + variable1 + player.getName()));
							return true;
						}

					}

					else {
						// Permission message
						sender.sendMessage(
								ChatColor.DARK_RED + "Uh oh! You don't have permission to heal that player!");
						return true;
					}
				}

			}
		}
		return true;
	}
}