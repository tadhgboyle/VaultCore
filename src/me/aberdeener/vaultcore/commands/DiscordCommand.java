package me.aberdeener.vaultcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;
import net.md_5.bungee.api.ChatColor;

public class DiscordCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String string = ChatColor.translateAlternateColorCodes('&', VaultCore.getInstance().getConfig().getString("string"));
		String variable1 = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("variable-1"));

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("discord")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			
			p.sendMessage(string + "Want to join our discord? Click here: " + variable1
					+ "https://discordapp.com/invite/5Vwbfbj");
			return true;
		}
		return true;

	}
}
