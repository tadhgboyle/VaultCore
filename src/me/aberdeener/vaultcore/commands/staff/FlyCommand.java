package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;
import net.md_5.bungee.api.ChatColor;

public class FlyCommand implements CommandExecutor {

	private boolean active = false;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

		if ((sender instanceof Player)) {

			Player p = (Player) sender;

			if (p.hasPermission("vc.fly")) {
				
				if (args.length == 0) {
					
					if (!active) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								string + "You have " + variable1 + "enabled" + string + " fly."));

						this.active = true;
						p.setAllowFlight(true);
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								string + "You have " + variable1 + "disabled" + string + " fly."));
						active = false;
						p.setFlying(false);
						p.setAllowFlight(false);
					}
				}

				else if (p.hasPermission("vc.fly.other")) {

					if (args.length == 1) {

						Player target = Bukkit.getPlayer(args[0]);
						if (target != null) {
							if (!active) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have "
										+ variable1 + "enabled" + string + " fly for " + variable1 + target.getName()));
								this.active = true;
								target.setAllowFlight(true);
								target.sendMessage(ChatColor.translateAlternateColorCodes('&',
										string + "Your fly has been " + variable1 + "enabled" + string + " by "
												+ variable1 + sender.getName()));
							}
							else {
								p.sendMessage(
										ChatColor.translateAlternateColorCodes('&', string + "You have " + variable1
												+ "disabled" + string + " fly for " + variable1 + target.getName()));
								active = false;
								target.setFlying(false);
								target.setAllowFlight(false);
								target.sendMessage(ChatColor.translateAlternateColorCodes('&',
										string + "Your fly has been " + variable1 + "disabled" + string + " by "
												+ variable1 + sender.getName()));
							}
						}

						else {
							p.sendMessage(ChatColor.RED + "That player is not online!");
						}
					}
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
				}
			}
			else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
			}
		}
		else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					VaultCore.getInstance().getConfig().getString("console-error")));
		}
		return true;
	}
}