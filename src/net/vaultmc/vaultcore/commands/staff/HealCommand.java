package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class HealCommand implements CommandExecutor {

	String string = VaultCore.getInstance().getConfig().getString("string");
	String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("heal")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("vc.heal")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (args.length == 0) {

				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', string + "You have been " + variable1 + "healed"));
				double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
				player.setHealth(maxHealth);
				player.setFoodLevel(20);
				player.setSaturation(20);
				return true;
			}
			if (args.length == 1) {

				if (player.hasPermission("vc.heal.other")) {

					Player target = Bukkit.getPlayer(args[0]);

					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					if (target == sender) {
						sender.sendMessage(ChatColor.DARK_GREEN + "Heal yourself using: " + ChatColor.RED + "/heal");
						return true;
					}
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "You have healed " + variable1 + target.getName()));
					target.setFoodLevel(20);
					target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					target.setSaturation(20);
					target.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "You have been healed by " + variable1 + player.getName()));
					return true;

				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Uh oh! You don't have permission to heal that player!");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/heal [player]");
				return true;
			}
		}
		return true;
	}
}