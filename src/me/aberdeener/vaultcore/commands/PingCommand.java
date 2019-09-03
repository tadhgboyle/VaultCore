package me.aberdeener.vaultcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;
import net.minecraft.server.v1_14_R1.EntityPlayer;

public class PingCommand implements CommandExecutor {

	public static int getPing(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
		return ep.ping;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
		String variable2 = VaultCore.getInstance().getConfig().getString("variable-2");

		if (command.getName().equalsIgnoreCase("ping")) {

			// console sender check
			if (!(sender instanceof Player)) {

				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "You can't get the ping of the console...");
					sender.sendMessage(ChatColor.RED + "Try adding a name after the command ;D");
					return true;
				}

				else if (args.length == 1) {
					Player target = Bukkit.getServer().getPlayer(args[0]);
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + target.getName()
							+ string + "" + "'s ping is: " + variable2 + "" + getPing(target) + string + "ms"));
					return true;
				}
			}

			// permission check
			Player p = (Player) sender;
			if (!sender.hasPermission("vc.ping")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));

				return true;

			} else {

				if (args.length == 0) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "" + "Your ping is: " + variable2 + "" + getPing(p) + string + "ms"));
					return true;

				} else if (args.length == 1) {
					if (!sender.hasPermission("vc.ping.other")) {
						sender.sendMessage(
								ChatColor.DARK_RED + "Uh oh! You don't have permission to check their ping!");
						return true;
					}

					Player target = Bukkit.getServer().getPlayer(args[0]);
					if (target == null) {
						sender.sendMessage(ChatColor.RED + "That player is offline!");
						return true;
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + target.getName()
							+ string + "" + "'s ping is: " + variable2 + "" + getPing(target) + string + "ms"));
					return true;
				}

				else
					sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/ping [PLAYER]");
				return true;
			}
		}
		return true;
	}
}