package net.vaultmc.vaultcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;

public class WorldTPCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		String string = ChatColor.translateAlternateColorCodes('&', VaultCore.getInstance().getConfig().getString("string"));
		String variable1 = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("variable-1"));

		Player player = (Player) sender;

		if (commandLabel.equalsIgnoreCase("sv")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			if (!sender.hasPermission(Permissions.WorldTPCommandSurvival)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			else {
				Location sv = (Location) VaultCore.getInstance().getPlayerData()
						.get("players." + player.getUniqueId() + ".sv");
				if (sv == null) {
					player.sendMessage(string + "You have never joined this world before... Bringing you to spawn.");
					player.performCommand("mvtp Survival");
					return true;
				}
				else {
					player.teleport(sv);
					player.sendMessage(string + "Teleported you to the " + variable1 + "Survival" + string + " world.");
					return true;
				}
			}
		}
		if (commandLabel.equalsIgnoreCase("cr")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			if (!sender.hasPermission(Permissions.WorldTPCommandCreative)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			else {
				Location cr = (Location) VaultCore.getInstance().getPlayerData()
						.get("players." + player.getUniqueId() + ".cr");
				if (cr == null) {
					player.sendMessage(string + "You have never joined this world before... Bringing you to spawn.");
					player.performCommand("mvtp Creative");
					return true;
				}
				else {
					player.teleport(cr);
					player.sendMessage(
							string + "Teleported you to the " + variable1 + "Creative" + string + " world.");
					return true;
				}
			}
		}
		return true;
	}
}