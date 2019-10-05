package me.aberdeener.vaultcore.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class DiscordCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("discord")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			Player player = (Player) sender;

			try {
				player.sendMessage(
						string + "Your token: " + variable2 + TokenCommand.getToken(player.getUniqueId(), player));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			player.sendMessage(string + "Click here to join our guild: " + variable1 + "https://discord.vaultmc.net");
			return true;
		}
		return true;

	}
}
