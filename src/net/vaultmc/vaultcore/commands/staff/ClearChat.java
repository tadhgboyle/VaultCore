package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class ClearChat implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

		// base command
		if (commandLabel.equalsIgnoreCase("clearchat") || commandLabel.equalsIgnoreCase("cc")) {

			// console sender check
			if (sender instanceof ConsoleCommandSender) {

				// send 200 empty lines to only players not console
				for (int i = 0; i < 200; i++) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						player.sendMessage(" ");
					}
				}

				// completion message to executor
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You have cleared chat!"));
				// completion message to all online players
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
						variable1 + "CONSOLE "
								+ string + "has cleared chat!"));
			}

			// if the sender is not console then...
			else if (sender instanceof Player) {
				// permission check
				Player p = (Player) sender;
				if (!p.hasPermission("vc.clearchat")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
				} else {

					// send 200 empty lines to only players not console
					for (int i = 0; i < 200; i++) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.sendMessage(" ");
						}
					}

					// completion message to executor
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							string + "You have cleared chat!"));
					
					// completion message to all online players
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							string + "The chat has been cleared by "
									+ variable1 + sender.getName()
									+ string + "!"));
				}
			}

			return true;
		}
		return true;
	}
}